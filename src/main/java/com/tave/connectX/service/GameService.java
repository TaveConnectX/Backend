package com.tave.connectX.service;

import com.tave.connectX.api.DeepLearningClient;
import com.tave.connectX.dto.GameDto;
import com.tave.connectX.dto.GameEndDto;
import com.tave.connectX.dto.ReviewResponseDto;
import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.entity.Game;
import com.tave.connectX.entity.Review;
import com.tave.connectX.entity.User;
import com.tave.connectX.entity.game.Difficulty;
import com.tave.connectX.provider.JwtProvider;
import com.tave.connectX.repository.GameRepository;
import com.tave.connectX.repository.ReviewRepository;
import com.tave.connectX.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DeepLearningClient deepLearningClient;
    private final GameRepository gameRepository;
    private final JwtProvider jwtProvider;

    private final RankingService rankingService;

    /**
     * 랜덤으로 선공을 정해주는 메서드 1: 유저 2: 에이전트
     */
    private int setFirstTurn() {
        return (int) (Math.random() * 2) + 1;
    }

    /**
     * 토큰에서 유저 정보를 조회하는 메서드 게임 시작할때 유저를 로드하기 위해 사용
     */
    private User loadUser(HttpServletRequest request) {
        // 현재 유저 조회
        String token = jwtProvider.resolveToken(request);
        String account = jwtProvider.getAccount(token.split(" ")[1].trim());
        User user = userRepository.findByOauthId(account).get();
        return user;
    }

    /**
     * 게임 시작 요청시 최초로 호출됩니다.
     * 게임 엔티티를 생성하고 선공을 정하여 응답합니다
     */
    public GameDto startGame(HttpServletRequest request, Difficulty difficulty) {

        // 유저 정보를 로드하고 게임 엔티티를 생성합니다.
        Game game = new Game();
        game.insertUserFK(loadUser(request));
        game.insertDifficulty(difficulty);
        gameRepository.save(game);

        int[][] list = new int[6][7];
        GameDto gameDto = new GameDto();
        gameDto.setGameIdx(game.getGameIdx());
        gameDto.setList(list);
        gameDto.setDifficulty(difficulty);

        // 선공이 에이전트인 경우 결과를 반영하여 넘겨주고 첫번째 리뷰를 저장합니다.
        if(setFirstTurn() == 2){
            log.info("에이전트 선공");
            gameDto = deepLearningClient.processResult(gameDto);
            saveReview(gameDto);
        }else log.info("유저 선공");

        return gameDto;
    }

    /**
     * 유저와 에이전트 각각의 턴마다 리뷰를 저장하기 위한 메서드입니다.
     */
    private void saveReview(GameDto gameDto) {

        // 게임 로드
        Game game = gameRepository.findById(gameDto.getGameIdx()).get();

        Review review = new Review(game, gameDto.getTurn(), gameDto.toJsonString());

        reviewRepository.save(review);
    }

    /**
     * 게임 진행 메서드
     * 유저와 모델의 턴을 DB에 저장하고
     * 모델의 결과를 반환합니다.
     */
    public GameDto processGame(GameDto gameDto) {

        // 유저 턴 저장
        saveReview(gameDto);

        // 모델 결과 반영
        GameDto modelResult = deepLearningClient.processResult(gameDto);

        // 모델 턴 저장
        saveReview(modelResult);

        return modelResult;
    }

    @Transactional
    public ReturnRankingDto endGame(GameEndDto gameEndDto) {

        Game game = gameRepository.findById(gameEndDto.getGameIdx()).get();

        game.insertWinner(gameEndDto.getWinner());

        int winner = gameEndDto.getWinner();

        // 유저가 이긴 경우 마지막 결과를 저장
        if (winner == 1) {
            saveReview(gameEndDto);
        }
        
        // 최근 게임을 저장
        Long userIdx = game.getUserFk().getUserIdx();
        User user = userRepository.findById(userIdx).get();
        Long updateLastGameIdx = user.updateLastGameIdx(gameEndDto.getGameIdx());

        int point = 0;
        int defeat = 0, victory = 0;

        // HARD 난이도인 경우에는 승점 변동
        if (game.getDifficulty().name().equals("HARD")) {

            // 승리
            if (winner == 1) {
                point = 3;
                victory = 1;
                defeat = 0;
            }

            // 패배
            if (winner == 2) {
                point = -3;
                victory = 0;
                defeat = 1;
            }

            // 무승부
            if (winner == 0) {
                point = 1;
                victory = 0;
                defeat = 0;
            }

        }

        ReturnRankingDto returnRankingDto = rankingService.updateRanking(new UpdateRankingDto(userIdx, victory, defeat, point, game.getDifficulty()));
        return returnRankingDto;
    }

    public List<ReviewResponseDto> findReview(HttpServletRequest request) {

        User user = loadUser(request);

        Game game;
        try {
            game = gameRepository.findById(user.getLastGameIdx()).get();
        } catch (NoSuchElementException e) {
            return null;
        }
            List<Review> reviewList = reviewRepository.findAllByGameFk(game);

        ArrayList<ReviewResponseDto> reviewResponseDto = new ArrayList<>();
        reviewList.forEach(review -> reviewResponseDto.add(new ReviewResponseDto(review.getTurn(), review.jsonToList())));

        return reviewResponseDto;
    }


}

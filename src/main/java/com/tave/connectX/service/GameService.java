package com.tave.connectX.service;

import com.tave.connectX.api.DeepLearningClient;
import com.tave.connectX.dto.game.ContinueGameResponseDto;
import com.tave.connectX.dto.game.GameDto;
import com.tave.connectX.dto.game.GameEndDto;
import com.tave.connectX.dto.game.ReviewResponseDto;
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
        int defeat = 0, victory = 0, draw = 0;

        // HARD 난이도인 경우에는 승점 변동
        if (game.getDifficulty().name().equals("HARD")) {

            // 승리
            if (winner == 1) {
                point = 3;
                victory = 1;
                defeat = 0;
                draw = 0;
            }

            // 패배
            if (winner == 2) {
                point = -3;
                victory = 0;
                defeat = 1;
                draw = 0;
            }

            // 무승부
            if (winner == 0) {
                point = 1;
                victory = 0;
                defeat = 0;
                draw = 1;
            }

        }

        ReturnRankingDto returnRankingDto = rankingService.updateRanking(new UpdateRankingDto(userIdx, victory, defeat, draw, point, game.getDifficulty()));
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

    public ContinueGameResponseDto loadRecentGame(Long gameIdx) {

        Game game;
        try {
            game = gameRepository.findById(gameIdx).orElseThrow();
        } catch (NoSuchElementException e) {
            return new ContinueGameResponseDto(0,-1, gameIdx, null, null);
        }

        List<Review> reviewList = reviewRepository.findAllByGameFkOrderByTurn(game);

        int[][] firstGameState = reviewList.get(0).jsonToList();
        int totalTurn = reviewList.size();
        int firstTurn = 0;
        int nextTurn = 0;

        for (int i = 0; i < 7; i++) {
            if (firstGameState[5][i] != 0) {
                firstTurn = firstGameState[5][i];
                break;
            }
        }
        log.info("firstTurn : {} , totalTurn : {}", firstTurn, totalTurn);

        // 유저 선공이면서 다음 턴이 홀수 턴인 경우 유저가 할 턴임
        if (firstTurn == 1 ) {
            if(totalTurn%2 == 0) nextTurn = 1;
            else nextTurn = 2;
        }

        if (firstTurn == 2) {
            if(totalTurn%2 == 0) nextTurn = 2;
            else nextTurn = 1;
        }

        int[][] list = reviewList.get(reviewList.size() - 1).jsonToList();

        return new ContinueGameResponseDto(totalTurn,nextTurn, game.getGameIdx(), game.getDifficulty(),list);
    };


}

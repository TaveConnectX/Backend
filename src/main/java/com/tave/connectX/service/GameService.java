package com.tave.connectX.service;

import com.tave.connectX.api.DeepLearningClient;
import com.tave.connectX.dto.GameDto;
import com.tave.connectX.entity.Game;
import com.tave.connectX.entity.Review;
import com.tave.connectX.entity.User;
import com.tave.connectX.entity.review.Content;
import com.tave.connectX.entity.review.ReviewId;
import com.tave.connectX.provider.JwtProvider;
import com.tave.connectX.repository.GameRepository;
import com.tave.connectX.repository.ReviewRepository;
import com.tave.connectX.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DeepLearningClient deepLearningClient;
    private final GameRepository gameRepository;
    private final JwtProvider jwtProvider;
    private static Long REVIEW_IDX = 1l;

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
        String account = jwtProvider.getAccount(token);
        User user = userRepository.findByOauthId(account).get();
        return user;
    }

    /**
     * 게임 시작 요청시 최초로 호출됩니다.
     * 게임 엔티티를 생성하고 선공을 정하여 응답합니다
     */
    public GameDto startGame(HttpServletRequest request) {

        // 유저 정보를 로드하고 게임 엔티티를 생성합니다.
        Game game = new Game();
        game.setUserFk(loadUser(request));
        gameRepository.save(game).getGameIdx();

        int[][] list = new int[6][7];
        GameDto gameDto = new GameDto();
        gameDto.setGameIdx(game.getGameIdx());
        gameDto.setList(list);

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

        // 컨텐츠 생성
        Content content = new Content(gameDto.toReviewContent());
        ReviewId reviewId = new ReviewId();
        reviewId.setId(REVIEW_IDX++);

        Review review = new Review(game, gameDto.getTurn(), gameDto.toJsonString());
        review.setReviewId(reviewId);

        reviewRepository.save(review);
    }

    /**
     * 게임 진행 메서드
     * 유저와 모델의 턴을 DB에 저장하고
     * 모델의 결과를 반환합니다.
     * 각 턴 저장 이후 승패를 판단하는 로직이 추가되어야 할 것 같습니다!
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

}

package com.tave.connectX.api;

import com.tave.connectX.dto.game.GameDto;
import com.tave.connectX.entity.Recommendation;
import com.tave.connectX.entity.game.Difficulty;
import com.tave.connectX.repository.GameRepository;
import com.tave.connectX.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class DeepLearningClient {

    private final RestTemplate restTemplate;
    private final RecommendationRepository recommendationRepository;
    private final GameRepository gameRepository;
    @Value(value = "${secret.deepLearning.uri}")
    private String BASE_URI;

    public GameDto processResult(GameDto gameDto) {

        int[][] map = gameDto.getList();

        Integer result = queryModel(map, gameDto.getDifficulty());

        if (result == -1) {
            return new GameDto(map, gameDto.getTurn(), gameDto.getGameIdx(),gameDto.getDifficulty());
        }

        updateMap(map, result);

        int[][] swap = gameDto.swapArray();
        Integer recommendedResult = queryModel(swap, gameDto.getDifficulty());

        updateMap(swap, result);
        Recommendation recommendation = new Recommendation(gameRepository.findById(gameDto.getGameIdx()).get(), GameDto.toJsonString(swap), gameDto.getTurn() + 1);
        recommendationRepository.save(recommendation);

        return new GameDto(map, gameDto.getTurn() + 1, gameDto.getGameIdx(),gameDto.getDifficulty());
    }

    private static void updateMap(int[][] map, Integer result) {
        // 모델 결과 반영하기, 모델이 반환한 열의 가장 위에 돌을 둡니다.
        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i][result] == 0) {
                map[i][result] = 2;
                break;
            }
        }
    }

    private Integer queryModel(int[][] map, Difficulty difficulty) {

        try {

            String requestUri = BASE_URI + "/get_next_action/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HashMap<String, Object> modelParameter = new HashMap<>();
            modelParameter.put("list", map);
            modelParameter.put("difficulty", difficulty.name());
            HttpEntity<HashMap<String, Object>> body = new HttpEntity<>(modelParameter, headers);

            long start = System.currentTimeMillis();
            Integer result = restTemplate.exchange(requestUri, HttpMethod.POST, body, Integer.class).getBody();
            long end = System.currentTimeMillis();
            log.info("Read time : {}ms", end - start);

            return result;
        } catch (RuntimeException e) {
            log.error("[queryModel] : ", e);
            return -1;
        }
    }
}
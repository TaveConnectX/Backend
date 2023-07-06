package com.tave.connectX.api;

import com.tave.connectX.dto.GameDto;
import com.tave.connectX.entity.game.Difficulty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.HashMap;
import java.util.Timer;


@Slf4j
@RequiredArgsConstructor
@Component
public class DeepLearningClient {

    private final RestTemplate restTemplate;
    @Value(value = "${secret.deepLearning.uri}")
    private String BASE_URI;

    public GameDto processResult(GameDto gameDto) {

        int[][] map = gameDto.getList();
        Integer result = queryModel(map, gameDto.getDifficulty());

        if (result == -1) {
            return new GameDto(map, gameDto.getTurn(), gameDto.getGameIdx(),gameDto.getDifficulty());
        }

        // 모델 결과 반영하기, 모델이 반환한 열의 가장 위에 돌을 둡니다.
        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i][result] == 0) {
                map[i][result] = 2;
                break;
            }
        }

        return new GameDto(map, gameDto.getTurn() + 1, gameDto.getGameIdx(),gameDto.getDifficulty());
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
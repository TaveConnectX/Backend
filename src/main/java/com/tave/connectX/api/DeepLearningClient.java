package com.tave.connectX.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.connectX.dto.GameDto;
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
    @Value(value = "${secret.deepLearning.uri}")
    private String BASE_URI;

    public GameDto processResult(GameDto gameDto) {

        int[][] map = gameDto.getList();
        Integer result = queryModel(map);

        if (result == -1) {
            return new GameDto(map, gameDto.getNow(), gameDto.getTurn(), gameDto.getGameIdx());
        }

        // 모델 결과 반영하기, 모델이 반환한 열의 가장 위에 돌을 둡니다.
        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i][result] == 0) {
                map[i][result] = 2;
                break;
            }
        }

        return new GameDto(map, result,gameDto.getTurn() + 1, gameDto.getGameIdx());
    }

    private Integer queryModel(int[][] map) {

        try {

            String requestUri = BASE_URI + "/get_next_action/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HashMap<String, Object> stringHashMap = new HashMap<>();
            stringHashMap.put("list", map);

            HttpEntity<HashMap<String, Object>> body = new HttpEntity<>(stringHashMap, headers);

            Integer result = restTemplate.exchange(requestUri, HttpMethod.POST, body, Integer.class).getBody();

            return result;
        } catch (RuntimeException e) {
            log.error("[queryModel] : ", e);
            return -1;
        }
    }

}
package com.tave.connectX.dto.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tave.connectX.entity.game.Difficulty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor
@Data
public class GameDto {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private int[][] list;   // 게임 맵
    private int turn;       // 턴 수
    private Long gameIdx;

    private Difficulty difficulty;

    public GameDto(int[][] list, int turn, Long gameIdx, Difficulty difficulty) {
        this.list = list;
        this.turn = turn;
        this.gameIdx = gameIdx;
        this.difficulty = difficulty;
    }

    public Map toReviewContent(){
        Map<String, String> map = new HashMap<>();

        try {
            map.put("list", objectMapper.writeValueAsString(this.list));
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return map;
    }

    public String toJsonString() {
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(this.list);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return jsonString;
    }

    }

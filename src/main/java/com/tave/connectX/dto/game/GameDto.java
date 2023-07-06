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

    public static String toJsonString(int[][] list) {
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return jsonString;
    }
    public int[][] swapArray() {
        int arr[][] = this.list;
        int swap[][] = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] != 0) {
                    swap[i][j] = (arr[i][j] == 1) ? 2 : 1;
                }
            }
        }
        return swap;
    }
}

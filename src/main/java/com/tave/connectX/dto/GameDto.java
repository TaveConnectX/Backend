package com.tave.connectX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GameDto {
    private int[][] list; // 게임 맵
    private int now;    // 방금 둔 위치

    public GameDto(int[][] list, int now) {
        this.list = list;
        this.now = now;
    }
}

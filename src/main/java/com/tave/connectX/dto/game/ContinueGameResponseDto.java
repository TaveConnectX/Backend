package com.tave.connectX.dto.game;

import com.tave.connectX.entity.game.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ContinueGameResponseDto {

    int totalTurnCount;
    int isNextTurn;       // 현재 턴
    Long gameIdx;
    Difficulty difficulty;
    int[][] list;   // 현재 게임 상태

}

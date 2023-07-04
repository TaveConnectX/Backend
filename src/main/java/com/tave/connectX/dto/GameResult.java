package com.tave.connectX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameResult{

    private int winner;
    private int winCnt;
    private int loseCnt;

}

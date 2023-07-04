package com.tave.connectX.dto.ranking;

import lombok.Data;

@Data
public class UpdateRankingDto {
    private int userIdx;
    private int victory;
    private int defeat;
    private int point;
}

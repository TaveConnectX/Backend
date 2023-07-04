package com.tave.connectX.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateRankingDto {
    private Long userIdx;
    private int victory;
    private int defeat;
    private int point;
}

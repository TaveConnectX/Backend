package com.tave.connectX.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReturnRankingDto {
    private int victory;
    private int defeat;
    private int point;
}

package com.tave.connectX.dto.ranking;

import lombok.Data;

@Data
public class GetRankingDto {
    private int ranking;
    private int victory;
    private int defeat;
    private int draw;
    private String name;
    private String picture;
}

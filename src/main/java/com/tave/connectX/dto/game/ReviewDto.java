package com.tave.connectX.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDto {
    private int first;
    private int turn;
    private int[][] list;
    private Integer recommendation;
}

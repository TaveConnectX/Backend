package com.tave.connectX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReviewResponseDto {

    private int turn;
    private int[][] list;

}

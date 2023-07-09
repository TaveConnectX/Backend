package com.tave.connectX.dto.ranking;

import com.tave.connectX.entity.game.Difficulty;
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
    private int draw;
    private int point;
    private Difficulty difficulty;

    public UpdateRankingDto(Long userIdx, int victory, int defeat, int draw, int point) {
        this.userIdx = userIdx;
        this.victory = victory;
        this.defeat = defeat;
        this.draw = draw;
        this.point = point;
    }
}

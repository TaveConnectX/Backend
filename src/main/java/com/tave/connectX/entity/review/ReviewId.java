package com.tave.connectX.entity.review;

import com.tave.connectX.entity.Game;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 복합 키 식별자
 */
@NoArgsConstructor
@Data
@Embeddable
public class ReviewId implements Serializable {

    private Long gameId;

    @Column(name = "review_idx")
    private Long id;

}

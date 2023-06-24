package com.tave.connectX.entity;

import com.tave.connectX.entity.review.ReviewId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ReviewId reviewId;

    @ManyToOne
    @JoinColumn(name = "game_idx")
    private Game gameFk;

    @Column
    private int turn;

    @Column(name = "content")
    private String content;

//    @JsonSubTypes.Type(Content.class)
//    @Column(columnDefinition = "json")
//    private Object content;

    public Review(Game gameFk, int turn, String content) {
        this.gameFk = gameFk;
        this.turn = turn;
        this.content = content;
    }
}

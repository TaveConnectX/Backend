package com.tave.connectX.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Recommendation_idx")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_idx")
    private Game game;

    @Column
    private String content;

    @Column
    private int turn;

    public Recommendation(Game game, String content, int turn) {
        this.game = game;
        this.content = content;
        this.turn = turn;
    }
}

package com.tave.connectX.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_idx")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_idx")
    private Game game;

    @Column(name = "recommended_column")
    private int recommendColumn;

    @Column
    private int turn;

    public Recommendation(Game game, int recommendColumn, int turn) {
        this.game = game;
        this.recommendColumn = recommendColumn;
        this.turn = turn;
    }
}

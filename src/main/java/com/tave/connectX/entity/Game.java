package com.tave.connectX.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Game {
    @Id
    @Column(name = "game_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameIdx;

    @Column(name = "is_winner")
    private int isWinner;

    @Column
    private int difficulty;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User userFk;

    public Game(int isWinner, int difficulty, User userFk) {
        this.isWinner = isWinner;
        this.difficulty = difficulty;
        this.userFk = userFk;
    }
}

package com.tave.connectX.entity;

import com.tave.connectX.entity.game.Difficulty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Game {
    @Id
    @Column(name = "game_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameIdx;

    @Column(name = "is_winner")
    private int isWinner;

    @Column
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User userFk;

    @OneToMany(mappedBy = "game")
    private List<Recommendation> recommendations;

    @OneToMany(mappedBy = "gameFk")
    private List<Review> reviews;


    public Game(int isWinner, Difficulty difficulty, User userFk) {
        this.isWinner = isWinner;
        this.difficulty = difficulty;
        this.userFk = userFk;
    }

    public void insertWinner(int isWinner) {
        this.isWinner = isWinner;
    }

    public void insertUserFK(User userFk) {
        this.userFk = userFk;
    }

    public void insertDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

}

package com.tave.connectX.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Percentage {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User userFk;

    @Column
    private int victory;

    @Column
    private int defeat;

    @Column
    private int points;

    public Percentage(User userFk, int victory, int defeat) {
        this.userFk = userFk;
        this.victory = victory;
        this.defeat = defeat;
    }

    public int updatePoints(int points) {
        this.points = points;
        return this.points;
    }

}

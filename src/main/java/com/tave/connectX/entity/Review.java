package com.tave.connectX.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.tave.connectX.entity.review.Content;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Review {
    @Id
    @ManyToOne
    @JoinColumn(name = "game_idx")
    private Game gameFk;

    @Column
    private int turn;

    @JsonSubTypes.Type(Content.class)
    @Column(columnDefinition = "json")
    private Object content;
}

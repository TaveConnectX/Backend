package com.tave.connectX.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @Column(name = "review_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

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

    public int[][] jsonToList() {

        int[][] list;

        try {
                list = objectMapper.readValue(this.content, int[][].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        return list;
    }

}

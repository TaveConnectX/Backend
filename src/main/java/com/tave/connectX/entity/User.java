package com.tave.connectX.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column
    private String name;

    @Column
    private String role;

    @Column(name = "last_game_idx")
    private Long lastGameIdx;

    public User(String oauthId, String name) {
        this.oauthId = oauthId;
        this.name = name;
    }

    public Long updateLastGameIdx(Long lastGameIdx) {
        this.lastGameIdx = lastGameIdx;
        return lastGameIdx;
    }
}

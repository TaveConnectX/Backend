package com.tave.connectX.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class User {
    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(name = "oauth_id")
    private Long oauthId;

    @Column
    private String name;

    @Column
    private String role;

    public User(Long oauthId, String name) {
        this.oauthId = oauthId;
        this.name = name;
    }
}

package com.tave.connectX.entity;

import com.tave.connectX.entity.role.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column
    private String name;

    @Column
    private Role role;

    public User(String name, Role role) {
        this.name = name;
        this.role = role;
    }
}

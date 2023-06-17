package com.tave.connectX.repository;

import com.tave.connectX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<User, Long> {
    User findUserByOauthId(String oauthId);
}

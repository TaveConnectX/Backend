package com.tave.connectX.repository;


import com.tave.connectX.entity.Game;
import com.tave.connectX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByUserFk(User user);

    @Query("select distinct g from Game g left join fetch g.reviews where g.id = :id")
    Optional<Game> findByIdFetch(@Param("id") Long id);

}

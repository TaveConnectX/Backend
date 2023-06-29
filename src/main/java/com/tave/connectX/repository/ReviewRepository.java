package com.tave.connectX.repository;


import com.tave.connectX.entity.Game;
import com.tave.connectX.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByGameFk(Game game);

}

package com.tave.connectX.repository;


import com.tave.connectX.entity.Review;
import com.tave.connectX.entity.review.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
}

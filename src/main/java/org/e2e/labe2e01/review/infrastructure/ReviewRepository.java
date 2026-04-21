package org.e2e.labe2e01.review.infrastructure;

import org.e2e.labe2e01.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRating(Integer rating);
    List<Review> findByAuthorId(Long authorId);
    Long countByAuthorId(Long authorId);
}

package com.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.review.model.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>  {
    
    @Query(value = "SELECT * FROM PRODUCT_REVIEW WHERE name = ?1", nativeQuery = true)
    Optional<ProductReview> findByProductName(String productName);
}

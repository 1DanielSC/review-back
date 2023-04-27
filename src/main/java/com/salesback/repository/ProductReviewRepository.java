package com.salesback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesback.model.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>  {
    
    Optional<ProductReview> findByProductName(String productName);
}

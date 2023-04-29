package com.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.review.exception.APIConnectionError;
import com.review.exception.NotFoundException;
import com.review.model.ProductReview;
import com.review.model.Review;
import com.review.model.dto.ProductDTO;
import com.review.repository.ProductReviewRepository;

import org.springframework.http.HttpStatus;

@Service
public class ProductReviewService {

    @Autowired
    public ProductReviewRepository repository;

    private ReviewResilience reviewResilience;
    
    @Autowired
    public ProductReviewService(ReviewResilience reviewResilience){
        this.reviewResilience = reviewResilience;
    }

    public ProductReview findById(Long id){
        Optional<ProductReview> review = repository.findById(id);
        return review.isPresent() ? review.get() : null;
    }

    public List<ProductReview> findAll(){
        return repository.findAll();
    }

    public ProductReview save(Review review){
        ProductReview productReview = findByName(review.getProductName());

        if(productReview!=null){
            review.setId(null);
            productReview.addReview(review);
            
            Double rating = productReview.getReviews().stream().mapToDouble(Review::getRating).sum();
            productReview.setRating(rating/(productReview.getReviews().size()*1.0));
            return repository.save(productReview);
        }

        ResponseEntity<ProductDTO> response = reviewResilience.getProductByName(review.getProductName());

        if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            productReview = new ProductReview();
            productReview.addReview(review);
            productReview.setProductName(review.getProductName());
            productReview.setRating(review.getRating());
            return repository.save(productReview);
        }
        else if(response.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new NotFoundException("Product not Found!");
        else
            throw new APIConnectionError("Error on communication with product-backend.");
    }


    public ProductReview findByName(String name){
        Optional<ProductReview> review = repository.findByProductName(name);
        return review.isPresent() ? review.get() : null;
    }

    public ProductReview save(ProductReview entity){
        return repository.save(entity);
    }
}

package com.salesback.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.salesback.model.Review;
import com.salesback.model.dto.ProductDTO;
import com.salesback.repository.ReviewRepository;


@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    private ReviewResilience reviewResilience;

    @Autowired
    public ReviewService(ReviewResilience reviewResilience){
        this.reviewResilience = reviewResilience;
    }

    public Review save(Review review){
        ResponseEntity<ProductDTO> response = reviewResilience.getProductByName(review.getProductName());

        if(response.getStatusCode() == HttpStatus.OK){
            ProductDTO product = response.getBody();

            if(product != null){
                product.setReview(review.getReview());
                product.setRating(review.getRating());
                
                ResponseEntity<ProductDTO> requestUpdate = reviewResilience.updateProduct(product);

                if(requestUpdate.getStatusCode() == HttpStatus.OK){
                    return reviewRepository.save(review);
                }
            }

            System.out.println("Error on updating product.");
            return null;
        }
        else{
            System.out.println("Product not Found!");
            return null;
        }
    }

    public Review findById(Long id){
        Optional<Review> review = reviewRepository.findById(id);
        return review.isPresent() ? review.get() : null;
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }

}

package com.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.review.model.ProductReview;
import com.review.model.Review;
import com.review.model.dto.ProductDTO;
import com.review.repository.ReviewRepository;


@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductReviewService productReviewService;

    private ReviewResilience reviewResilience;

    @Autowired
    public ReviewService(ReviewResilience reviewResilience){
        this.reviewResilience = reviewResilience;
    }

    /*
    public Review save(Review review){
        ResponseEntity<ProductDTO> response = reviewResilience.getProductByName(review.getProductName());

        if(response.getStatusCode() == HttpStatus.OK){
            ProductDTO product = response.getBody();

            if(product != null){
                product.setReview(review.getReview().strip());
                product.setRating(review.getRating());
                
                //ResponseEntity<ProductDTO> requestUpdate = reviewResilience.updateProduct(product);

                //if(requestUpdate.getStatusCode() == HttpStatus.OK){
                   return reviewRepository.save(review);
                //}
            }

            System.out.println("Error on updating product.");
            return null;
        }
        else{
            System.out.println("Product not Found!");
            return null;
        }
    }
     */

    public ProductReview save(Review review){
        ProductReview revieww = productReviewService.findByName(review.getProductName());

        if(revieww!=null){
            List<Review> reviews = revieww.getReviews();
            reviews.add(review);
            Double rating = reviews.stream().mapToDouble(Review::getRating).sum();
            revieww.setRating(rating/(reviews.size()*1.0));
            return productReviewService.save(revieww);
        }

        ResponseEntity<ProductDTO> response = reviewResilience.getProductByName(review.getProductName());

        if(response.getStatusCode() == HttpStatus.OK){
            revieww = new ProductReview();
            revieww.addReview(review);
            revieww.setProductName(review.getProductName());
            revieww.setRating(review.getRating());
            return productReviewService.save(revieww);
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

    public Review findByName(String name){
        Optional<Review> review = reviewRepository.findByProductName(name);
        return review.isPresent() ? review.get() : null;
    }

}

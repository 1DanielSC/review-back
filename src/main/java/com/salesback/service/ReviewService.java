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
import com.salesback.service.interfaces.ProductServiceInterface;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    private ProductServiceInterface repository;

    @Autowired
    public ReviewService(ProductServiceInterface psi){
        this.repository = psi;
    }

    @CircuitBreaker(name = "servicebeta")
    public ResponseEntity<ProductDTO> getProductByName(String productName){
        return repository.findProductByName(productName);
    }

    @CircuitBreaker(name = "servicebeta")
    public ResponseEntity<ProductDTO> updateProduct(ProductDTO product){
        return repository.updateProduct(product);
    }

    //@CircuitBreaker(name = "servicebeta")
    public Review save(Review review){

        ResponseEntity<ProductDTO> response = getProductByName(review.getProductName());

        if(response.getStatusCode() == HttpStatus.OK){
            ProductDTO product = response.getBody();

            if(product != null){
                product.setReview(review.getReview());
                product.setRating(review.getRating());
                
                ResponseEntity<ProductDTO> requestUpdate = updateProduct(product);

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

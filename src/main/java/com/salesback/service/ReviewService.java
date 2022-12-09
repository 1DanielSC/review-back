package com.salesback.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.salesback.model.Review;
import com.salesback.model.dto.ProductDTO;
import com.salesback.repository.ReviewRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    @CircuitBreaker(name = "servicebeta")
    public Review save(Review review){

        RestTemplate restTemplate = new RestTemplate();
        String url_getProduct = "http://localhost:8080/product/findByName/" + review.getProductName();

        ResponseEntity<ProductDTO> response = restTemplate.getForEntity(url_getProduct, ProductDTO.class);

        if(response.getStatusCode() == HttpStatus.OK){
            ProductDTO product = response.getBody();

            if(product != null){
                product.setReview(review.getReview());
                product.setRating(review.getRating());

                String url_updateProduct = "http://localhost:8080/product/update"; //TODO: Change here!
                
                HttpEntity<ProductDTO> request = new HttpEntity<>(product);

                ResponseEntity<ProductDTO> requestUpdate = restTemplate.exchange(url_updateProduct, HttpMethod.PUT, request, ProductDTO.class); //Problem

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

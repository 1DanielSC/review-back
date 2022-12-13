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

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    private ProductServiceInterface repository;

    @Autowired
    public ReviewService(ProductServiceInterface psi){
        this.repository = psi;
    }

    @CircuitBreaker(name = "getProductNameBreaker", fallbackMethod = "buildFallBack")
    @Retry(name = "retryservicebeta", fallbackMethod = "retryFallBack") 
    @Bulkhead(name = "getProductNameBulk", fallbackMethod = "getProductNameBulkheadFallBack") //type = Bulkhead.Type.SEMAPHORE
    public ResponseEntity<ProductDTO> getProductByName(String productName){
        return repository.findProductByName(productName);
    }

    @CircuitBreaker(name = "updateProductBreaker", fallbackMethod = "buildFallBack2")
    @Retry(name = "retryservicebeta", fallbackMethod = "retryFallBack")
    @Bulkhead(name = "updateProductBulk", fallbackMethod = "updateProductBulkheadFallBack")
    public ResponseEntity<ProductDTO> updateProduct(ProductDTO product){
        return repository.updateProduct(product);
    }

    public ResponseEntity<String> buildFallBack(String productName, Throwable t){
        System.err.println("FaALL BACK " + productName);
        return ResponseEntity.ok("Fallback in action");
    }

    public ResponseEntity<String> buildFallBack2(ProductDTO product, Throwable t){
        System.err.println("FaALL BACK2 " + product.getName());
        return ResponseEntity.ok("Fallback in action");
    }

    public ResponseEntity<String> getProductNameBulkheadFallBack(String productName, Throwable t){
        System.err.println("BULKHEAD - Falha no GET product " + productName);
        return ResponseEntity.ok("failllllllllll");
    } 

    public ResponseEntity<String> updateProductBulkheadFallBack(ProductDTO product, Throwable t){
        System.err.println("BULKHEAD - Falha no UPDATE product " + product.getName());
        return ResponseEntity.ok("failllllllllll");
    }

    public ResponseEntity<String> retryProductService(Throwable t){
        System.err.println("SERVIÇO CAIU - Falha no product ");
        return ResponseEntity.ok("failllllllllll");
    }    


    public Review save(Review review){

        ResponseEntity<ProductDTO> response = this.getProductByName(review.getProductName());

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

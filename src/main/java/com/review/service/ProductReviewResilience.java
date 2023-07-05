package com.review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.review.model.dto.ProductDTO;
import com.review.service.interfaces.ProductServiceClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ProductReviewResilience {

    @Autowired
    private ProductServiceClient productClient;

    @CircuitBreaker(name = "productservice", fallbackMethod = "circuitBreakerFallBack")
    @RateLimiter(name = "rate_productservice", fallbackMethod = "rateLimiterFallBack")
    @Bulkhead(name = "bulk_productservice", fallbackMethod = "bulkheadFallBack")
    @Retry(name = "retry_productservice", fallbackMethod = "retryFallBack")
    public ResponseEntity<ProductDTO> requestProductByName(String name){
        return productClient.findProductByName(name);
    }

    public ResponseEntity<ProductDTO> circuitBreakerFallBack(Throwable throwable){
        System.out.println("Circuit Breaker (fallback): " + throwable.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<ProductDTO> rateLimiterFallBack(Throwable throwable){
        System.out.println("Rate Limiter (fallback): " + throwable.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<ProductDTO> bulkheadFallBack(Throwable throwable){
        System.out.println("Bulkhead (fallback): " + throwable.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<ProductDTO> retryFallBack(Throwable throwable){
        System.out.println("Retry (fallback): " + throwable.getMessage());
        return ResponseEntity.internalServerError().build();
    }

}

package com.review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.review.model.dto.ProductDTO;
import com.review.service.interfaces.ProductServiceInterface;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class ReviewResilience {
    
    private ProductServiceInterface repository;

    @Autowired
    public ReviewResilience(ProductServiceInterface repository){
        this.repository = repository;
    }

    @CircuitBreaker(name = "getProductNameBreaker", fallbackMethod = "buildFallBack")
    @Retry(name = "retryservicebeta", fallbackMethod = "retryFallBack") 
    @Bulkhead(name = "getProductNameBulk", fallbackMethod = "getProductNameBulkheadFallBack") //type = Bulkhead.Type.SEMAPHORE
    public ResponseEntity<ProductDTO> getProductByName(String productName){
        return repository.findProductByName(productName);
    }

    public ResponseEntity<String> buildFallBack(String productName, Throwable t){
        System.err.println("FaALL BACK " + productName);
        return ResponseEntity.ok("Fallback in action");
    }

    public ResponseEntity<String> getProductNameBulkheadFallBack(String productName, Throwable t){
        System.err.println("BULKHEAD - Search by name failed for: " + productName);
        System.out.println(t.getMessage());

        if(t.getMessage().contains("404"))
            return ResponseEntity.notFound().build();
        else if(t.getMessage().contains("503"))
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        return ResponseEntity.internalServerError().build();
    } 

    public ResponseEntity<String> retryFallBack(Throwable t){
        System.err.println("SERVIÇO CAIU - Falha no product");
        return ResponseEntity.ok("failllllllllll");
    }

}

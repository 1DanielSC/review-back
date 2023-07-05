package com.review.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ProductReviewResilience productRequest;


    @Cacheable(value  = "reviews", key = "#id")
    @Transactional(readOnly = true)
    public ProductReview findById(Long id){
        System.out.println("Vou buscar o review por id no banco...");
        return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Review not found with the ID provided."));
    }

    @Cacheable(value  = "reviews")
    public List<ProductReview> findAll(){
        return repository.findAll();
    }

    @CacheEvict(value = "reviews", allEntries = true)
    public ProductReview save(ProductReview entity){
        return repository.save(entity);
    }

    private ProductReview requestProductByName(Review review){
        
        ResponseEntity<ProductDTO> response = productRequest.requestProductByName(review.getProductName());
        
        ProductReview productReview = null;
        if(response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            productReview = new ProductReview();
            productReview.addReview(review);
            productReview.setProductName(review.getProductName());
            productReview.setRating(review.getRating());
            return save(productReview);
        }
        else if(response.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new NotFoundException("Product not Found!");
        else
            throw new APIConnectionError("Error on communication with product-backend.");
    }

    public ProductReview save(Review review){
        ProductReview productReview = null;
        
        try {
            productReview = findByName(review.getProductName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(productReview!=null){
            review.setId(null);
            productReview.addReview(review);
            
            Double rating = productReview.getReviews().stream().mapToDouble(Review::getRating).sum();
            productReview.setRating(rating/(productReview.getReviews().size()*1.0));
            return save(productReview);
        }

        return requestProductByName(review);
    }


    @Cacheable(value  = "reviews", key = "#name")
    @Transactional(readOnly = true)
    public ProductReview findByName(String name){
        System.out.println("Entrei aqui no findByname");
        return repository.findByProductName(name)
            .orElseThrow(() -> new NotFoundException("Product with name \"" + name + "\" was not found."));
    }

}

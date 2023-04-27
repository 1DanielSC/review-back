package com.salesback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesback.model.ProductReview;
import com.salesback.model.Review;
import com.salesback.service.ProductReviewService;

@Controller
@RequestMapping(value = "product-review")
public class ProductReviewController {
    
    @Autowired
    private ProductReviewService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductReview> findById(@PathVariable(value = "id") Long id){
        ProductReview review = service.findById(id);

        if(review != null)
            return ResponseEntity.ok(review);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductReview>> findAll(){
        List<ProductReview> reviews = service.findAll();

        if(reviews != null)
            return ResponseEntity.ok(reviews);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductReview> save(@RequestBody Review review){
        ProductReview reviewSaved = service.save(review);

        if(reviewSaved != null)
            return ResponseEntity.ok(reviewSaved);
        else
            return ResponseEntity.noContent().build();
    }

}

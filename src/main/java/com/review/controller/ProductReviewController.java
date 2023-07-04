package com.review.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.review.model.ProductReview;
import com.review.model.Review;
import com.review.service.ProductReviewService;

@Controller
@RequestMapping(value = "product-review")
public class ProductReviewController {
    
    @Autowired
    private ProductReviewService service;

    @GetMapping("/thread")
    public void thread(){
        System.out.println(Thread.currentThread().toString());
    } 

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductReview> findById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(service.findById(id));
    }
 
    @GetMapping
    public ResponseEntity<List<ProductReview>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<ProductReview> save(@RequestBody Review review){
        return ResponseEntity.ok(service.save(review));
    }

}

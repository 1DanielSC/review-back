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

import com.salesback.model.Review;
import com.salesback.service.ReviewService;

@Controller
@RequestMapping(value = "review")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/findById/{id}")
    public ResponseEntity<Review> findById(@PathVariable(value = "id") Long id){
        Review review = reviewService.findById(id);

        if(review != null)
            return ResponseEntity.ok(review);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<List<Review>> findAll(){
        List<Review> reviews = reviewService.findAll();

        if(reviews != null)
            return ResponseEntity.ok(reviews);
        else
            return ResponseEntity.notFound().build();
    }


    @PostMapping(value = "/save")
    public ResponseEntity<Review> save(@RequestBody Review review){
        Review reviewSaved = reviewService.save(review);

        if(reviewSaved != null)
            return ResponseEntity.ok(reviewSaved);
        else
            return ResponseEntity.noContent().build();
    }


}

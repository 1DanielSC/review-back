package com.review.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.review.model.dto.ProductDTO;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/product/name/{name}")
    ResponseEntity<ProductDTO> findProductByName(@PathVariable(value = "name") String name);
}

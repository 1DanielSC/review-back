package com.review.service.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.review.model.dto.ProductDTO;

@FeignClient("PRODUCT")
public interface ProductServiceInterface {

    @RequestMapping(method = RequestMethod.GET, value = "/product/name/{name}")
    ResponseEntity<ProductDTO> findProductByName(@PathVariable(value = "name") String name);
}

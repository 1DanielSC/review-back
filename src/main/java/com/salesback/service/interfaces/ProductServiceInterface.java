package com.salesback.service.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesback.model.dto.ProductDTO;

@FeignClient("PRODUCT")
public interface ProductServiceInterface {

    @RequestMapping(method = RequestMethod.PUT, value = "/product/update")
    ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO product);

    @RequestMapping(method = RequestMethod.GET, value = "/product/findByName/{name}")
    ResponseEntity<ProductDTO> findProductByName(@PathVariable(value = "name") String name);
}

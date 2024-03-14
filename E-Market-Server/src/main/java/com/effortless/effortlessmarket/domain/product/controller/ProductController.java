package com.effortless.effortlessmarket.domain.product.controller;

import com.effortless.effortlessmarket.domain.product.dto.ProductRequest;
import com.effortless.effortlessmarket.domain.product.dto.ProductResponse;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        ProductResponse product = productService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long productId){
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 상품이 삭제되었습니다.");
    }

}

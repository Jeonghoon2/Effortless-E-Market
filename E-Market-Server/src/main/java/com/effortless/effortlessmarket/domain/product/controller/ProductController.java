package com.effortless.effortlessmarket.domain.product.controller;

import com.effortless.effortlessmarket.domain.product.dto.ProductRequest;
import com.effortless.effortlessmarket.domain.product.dto.ProductResponse;
import com.effortless.effortlessmarket.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @ModelAttribute ProductRequest productRequest
            ) throws IOException {
        ProductResponse product = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long productId){
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable("categoryId") Long categoryId,
            Pageable pageable) {
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/recent")
    public ResponseEntity<Page<ProductResponse>> getRecentProduct(){
        Page<ProductResponse> recentProduct = productService.getRecentProduct();

        return ResponseEntity.ok(recentProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 상품이 삭제되었습니다.");
    }

    @GetMapping("/all/py")
    public ResponseEntity<List<ProductResponse>> getAllProductPy(){
        List<ProductResponse> productList = productService.getAllProductPy();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

}

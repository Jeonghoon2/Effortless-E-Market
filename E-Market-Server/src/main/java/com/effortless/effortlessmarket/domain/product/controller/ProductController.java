package com.effortless.effortlessmarket.domain.product.controller;


import com.effortless.effortlessmarket.domain.product.dto.request.SaveProductRequest;
import com.effortless.effortlessmarket.domain.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity createProduct(@RequestBody @Valid SaveProductRequest request, HttpServletRequest servletRequest){

        return productService.createProduct(request, servletRequest.getRequestURI());

    }

}

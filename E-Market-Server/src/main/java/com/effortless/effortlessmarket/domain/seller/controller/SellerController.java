package com.effortless.effortlessmarket.domain.seller.controller;

import com.effortless.effortlessmarket.domain.seller.dto.request.SellerRequest;
import com.effortless.effortlessmarket.domain.seller.serivce.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/create")
    public ResponseEntity<SellerRequest> createSeller(@RequestBody @Valid SellerRequest request){
        return sellerService.createSeller(request);
    }

}

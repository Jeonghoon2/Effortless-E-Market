package com.effortless.effortlessmarket.domain.seller.controller;

import com.effortless.effortlessmarket.domain.seller.dto.SellerRequest;
import com.effortless.effortlessmarket.domain.seller.dto.SellerResponse;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.domain.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    /* 판매자 생성 */
    @PostMapping
    public Seller createSeller(@RequestBody SellerRequest sellerRequest){
        return sellerService.createSeller(sellerRequest);
    }

    /* 판매자 수정 */
    @PutMapping
    public ResponseEntity<Seller> updateSeller(@RequestBody SellerRequest sellerRequest){
        Seller seller = sellerService.updateSeller(sellerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(seller);
    }

    /* 판매자 삭제 */
    @DeleteMapping("/{sellerId}")
    public ResponseEntity<String> deleteSeller(@PathVariable("sellerId") Long sellerId){
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 회원 탈퇴 되었습니다.");
    }

    /* 모든 판매자 조회 */
    @GetMapping("/all")
    public ResponseEntity<List<SellerResponse>> getAllSeller(){
        List<SellerResponse> allSeller = sellerService.getAllerSeller();
        return ResponseEntity.status(HttpStatus.OK).body(allSeller);
    }

}

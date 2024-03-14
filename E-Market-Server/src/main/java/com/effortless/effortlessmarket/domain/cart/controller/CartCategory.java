package com.effortless.effortlessmarket.domain.cart.controller;

import com.effortless.effortlessmarket.domain.cart.dto.CartRequest;
import com.effortless.effortlessmarket.domain.cart.entity.Cart;
import com.effortless.effortlessmarket.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartCategory {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestBody CartRequest.addItem addItem){
        Cart cart = cartService.addCart(addItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<Cart>> getCart(@PathVariable("memberId") Long memberId){
        List<Cart> allCartItems = cartService.getAllCartItems(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(allCartItems);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteCartItem(@RequestBody CartRequest.deleteItem deleteItems){
        cartService.deleteItem(deleteItems);
        return ResponseEntity.status(HttpStatus.OK).body("정상 적으로 삭제 되었습니다.");
    }
}

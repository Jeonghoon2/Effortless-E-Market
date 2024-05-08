package com.effortless.effortlessmarket.domain.order.controller;

import com.effortless.effortlessmarket.domain.order.dto.OrderRequest;
import com.effortless.effortlessmarket.domain.order.dto.OrderResponse;
import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.domain.order.service.OrderService;
import com.effortless.effortlessmarket.global.annotaion.EnableLogging;
import com.effortless.effortlessmarket.global.annotaion.ExcludeLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest.Order orderRequest){
        OrderResponse order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @PostMapping("/cart")
    public ResponseEntity<Order> createCartInOrder(@RequestBody OrderRequest.FromCart fromCart){
        Order cartInOrder = orderService.createCartInOrder(fromCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartInOrder);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable("memberId") Long memberId){
        List<Order> orders = orderService.getOrders(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}

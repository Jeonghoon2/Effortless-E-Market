package com.effortless.effortlessmarket.domain.orderDetail.controller;

import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import com.effortless.effortlessmarket.domain.orderDetail.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orderDetail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping("{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetails(@PathVariable("orderId") Long orderId){
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDetails);
    }


}

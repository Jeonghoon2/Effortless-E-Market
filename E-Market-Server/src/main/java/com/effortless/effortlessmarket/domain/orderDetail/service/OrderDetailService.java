package com.effortless.effortlessmarket.domain.orderDetail.service;

import com.effortless.effortlessmarket.domain.order.repository.OrderRepository;
import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import com.effortless.effortlessmarket.domain.orderDetail.repository.OrderDetailRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;


    public List<OrderDetail> getOrderDetails(Long orderId){

        orderRepository.findById(orderId).orElseThrow(
                () -> new CustomExceptionType(CustomException.ORDER_NOT_FOUND)
        );

        return orderDetailRepository.findByOrderId(orderId);
    }

}

package com.effortless.effortlessmarket.domain.order.dto;

import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.domain.orderDetail.dto.OrderDetailResponse;
import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponse {

    private Long orderId;
    private Long memberId;
    private String recipientAddress;
    private LocalDateTime createAt;
    private List<OrderDetailResponse> orderDetailList = new ArrayList<>();

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.memberId = order.getMember().getId();
        this.recipientAddress = order.getMemberAddress().getRecipientAddress();
        this.createAt = order.getDate();

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                    .sellerId(orderDetail.getProduct().getSeller().getId())
                    .productId(orderDetail.getProduct().getId())
                    .productName(orderDetail.getProduct().getName())
                    .firstCategoryId(orderDetail.getProduct().getCategory().getParent().getParent().getId())
                    .secondCategoryId(orderDetail.getProduct().getCategory().getParent().getId())
                    .thirdCategoryId(orderDetail.getProduct().getCategory().getId())
                    .categoryName(orderDetail.getProduct().getCategory().getName())
                    .count(orderDetail.getCount())
                    .totalPrice(orderDetail.getProduct().getPrice() * orderDetail.getCount())
                    .build();

            this.orderDetailList.add(orderDetailResponse);
        }

    }
}

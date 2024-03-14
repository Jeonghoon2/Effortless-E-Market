package com.effortless.effortlessmarket.domain.orderDetail.repository;

import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);
}

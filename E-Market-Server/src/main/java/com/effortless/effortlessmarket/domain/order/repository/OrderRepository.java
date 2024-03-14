package com.effortless.effortlessmarket.domain.order.repository;

import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long id);
    List<Order> findByMemberId(Long id);

}

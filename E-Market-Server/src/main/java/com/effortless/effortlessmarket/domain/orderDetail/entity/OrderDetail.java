package com.effortless.effortlessmarket.domain.orderDetail.entity;

import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @Column(name = "order_detail_count")
    private Integer count;

    @Column(name = "order_detail_total_price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_detail_refund")
    private Refund refund;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;



    public OrderDetail(Order order, Product product, Integer count) {
        this.order = order;
        this.product = product;
        this.price = product.getPrice() * count;
        this.count = count;
    }
}

package com.effortless.effortlessmarket.domain.order.entity;

import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import com.effortless.effortlessmarket.domain.order.dto.OrderRequest;
import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_card_name", length = 20, nullable = false)
    private String cardName;

    @Column(name = "order_card_number", length = 50, nullable = false)
    private String cardNumber;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "order_date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_address_id")
    private MemberAddress memberAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void saveOrder(Member member, OrderStatus orderStatus, MemberAddress memberAddress, String cardName, String cardNumber){
        this.member = member;
        this.memberAddress = memberAddress;
        this.status = orderStatus;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.date = LocalDateTime.now();

    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
    }
}
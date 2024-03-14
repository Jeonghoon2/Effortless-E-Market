package com.effortless.effortlessmarket.domain.order.service;

import com.effortless.effortlessmarket.domain.cart.entity.Cart;
import com.effortless.effortlessmarket.domain.cart.repository.CartRepository;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.repository.MemberRepository;
import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import com.effortless.effortlessmarket.domain.memberAddress.respositroy.MemberAddressRepository;
import com.effortless.effortlessmarket.domain.order.dto.OrderRequest;
import com.effortless.effortlessmarket.domain.order.dto.OrderResponse;
import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.domain.order.entity.OrderStatus;
import com.effortless.effortlessmarket.domain.order.repository.OrderRepository;
import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.product.repository.ProductRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Transactional
    public Order createCartInOrder(OrderRequest.FromCart request) {
        Member member = getMember(request.getMemberId());
        MemberAddress memberAddress = memberAddressRepository.findById(request.getMemberAddressId()).orElseThrow(
                () -> new CustomExceptionType(CustomException.MEMBER_ADDRESS_NOT_FOUND)
        );
        Order order = initializeOrder(member, memberAddress,request.getCardName(), request.getCardNumber());

        request.getCartIds().forEach(cartId -> {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new CustomExceptionType(CustomException.CART_NOT_FOUND));
            addOrderDetailFromCart(cart, order);
        });

        orderRepository.save(order);
        clearCart(request.getCartIds());
        return order;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest.Order orderRequest) {

        Member member = getMember(orderRequest.getMemberId());

        MemberAddress memberAddress = memberAddressRepository.findById(orderRequest.getMemberAddressId()).orElseThrow(
                () -> new CustomExceptionType(CustomException.MEMBER_ADDRESS_NOT_FOUND)
        );

        Order order = initializeOrder(member, memberAddress,orderRequest.getCardName(), orderRequest.getCardNumber());

        orderRequest.getOrderList().forEach(detail -> {
            Product product = getProduct(detail.getProductId());
            addOrderDetail(order, product, detail.getCount());
        });

        Order createOrder = orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse(createOrder);





        return orderResponse;
    }

    public List<Order> getOrders(Long memberId) {
        List<Order> byMemberId = orderRepository.findByMemberId(memberId);
        return byMemberId;
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomExceptionType(CustomException.PRODUCT_NOT_FOUND));
    }

    private Order initializeOrder(Member member, MemberAddress memberAddress, String cardName, String cardNumber) {
        Order order = new Order();
        order.saveOrder(member, OrderStatus.READY, memberAddress, cardName, cardNumber);
        return order;
    }

    private void addOrderDetailFromCart(Cart cart, Order order) {
        Product product = cart.getProduct();
        addOrderDetail(order, product, cart.getCount());
    }

    private void addOrderDetail(Order order, Product product, Integer count) {
        if (product.getQuantity() - count < 0) {
            throw new CustomExceptionType(CustomException.PRODUCT_COUNT_SHORTAGE);
        }
        product.decreaseCount(count);
        OrderDetail orderDetail = new OrderDetail(order, product, count);
        order.addOrderDetail(orderDetail);
    }

    private void clearCart(List<Long> cartIds) {
        cartRepository.deleteAllByIdInBatch(cartIds);
    }


}




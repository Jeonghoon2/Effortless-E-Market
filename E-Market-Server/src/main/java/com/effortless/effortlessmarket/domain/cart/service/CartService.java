package com.effortless.effortlessmarket.domain.cart.service;

import com.effortless.effortlessmarket.domain.cart.dto.CartRequest;
import com.effortless.effortlessmarket.domain.cart.entity.Cart;
import com.effortless.effortlessmarket.domain.cart.repository.CartRepository;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.repository.MemberRepository;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.product.repository.ProductRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /* 카트 담기 */
    @Transactional
    public Cart addCart(CartRequest.addItem cartDto){

        Member member = memberRepository.findById(cartDto.getMemberId()).orElseThrow(
                () -> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND)
        );

        Product product = productRepository.findById(cartDto.getProductId()).orElseThrow(
                () -> new CustomExceptionType(CustomException.PRODUCT_NOT_FOUND)
        );

        /* 수량 체크 */
        if (product.getQuantity() < cartDto.getCount()) {
            throw new CustomExceptionType(CustomException.PRODUCT_COUNT_SHORTAGE);
        }

        /* 중복 담기 체크 */
        Optional<Cart> existingCart = cartRepository.findByMemberAndProduct(member, product);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.increaseCount(cartDto.getCount());
            product.decreaseCount(cartDto.getCount());
            return cart;
        } else {
            product.decreaseCount(cartDto.getCount());
            Cart cart = Cart.builder()
                    .member(member)
                    .product(product)
                    .count(cartDto.getCount())
                    .build();
            return cartRepository.save(cart);
        }
    }


    /* 카트에 담긴 모든 상품 가져오기 */
    @Transactional(readOnly = true)
    public List<Cart> getAllCartItems(Long memberId){
        return cartRepository.findByMemberId(memberId);
    }

    /* 카트 제거 */
    @Transactional
    public void deleteItem(CartRequest.deleteItem cartDto){
        cartDto.getCartIds().forEach(cartId -> {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new CustomExceptionType(CustomException.CART_NOT_FOUND));

            cart.getProduct().increaseCount(cart.getCount());
            cartRepository.deleteById(cartId);
        });
    }

}

package com.effortless.effortlessmarket.cart;

import com.effortless.effortlessmarket.domain.cart.dto.CartRequest;
import com.effortless.effortlessmarket.domain.cart.entity.Cart;
import com.effortless.effortlessmarket.domain.cart.repository.CartRepository;
import com.effortless.effortlessmarket.domain.cart.service.CartService;
import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.service.CategoryService;
import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.entity.GenderType;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.repository.MemberRepository;
import com.effortless.effortlessmarket.domain.member.service.MemberService;
import com.effortless.effortlessmarket.domain.product.dto.ProductRequest;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.product.repository.ProductRepository;
import com.effortless.effortlessmarket.domain.product.service.ProductService;
import com.effortless.effortlessmarket.domain.seller.dto.SellerRequest;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.domain.seller.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CartTest {

    @Autowired private CartService cartService;
    @Autowired private MemberService memberService;
    @Autowired private ProductService productService;
    @Autowired private SellerService sellerService;
    @Autowired private CategoryService categoryService;

    @Autowired private MemberRepository memberRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CartRepository cartRepository;

    private Member member;
    private Seller seller;
    private Category category;

    @BeforeEach
    void setup(){
        MemberRequest memberRequest = MemberRequest.builder()
                .name("테스터")
                .phoneNumber("0100000000")
                .email("tester01@gmail.com")
                .gender(GenderType.F)
                .password("password")
                .build();

        member = memberService.saveMember(memberRequest);

        SellerRequest sellerRequest= SellerRequest.builder()
                .name("TestSeller")
                .email("email@gmail.com")
                .password("1234")
                .brandName("전자상가")
                .phoneNumber("010000000")
                .build();

        seller = sellerService.createSeller(sellerRequest);

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .name("키보드")
                .build();

        category = categoryService.createCategory(categoryRequest);

        ProductRequest productRequest = ProductRequest.builder()
                .name("한성 키보드")
                .description("저렴해요")
                .price(20000)
                .quantity(100)
                .sellerId(seller.getId())
                .categoryId(category.getId())
                .build();

        productService.createProduct(productRequest);

    }

    @Test
    @Transactional
    void createCartTest(){
        Member member = memberRepository.findByEmail("tester01@gmail.com").get();
        Product product = productRepository.findByName("한성 키보드").get();

        CartRequest.addItem cartRequest = CartRequest.addItem.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .count(10)
                .build();

        Cart cart = cartService.addCart(cartRequest);

        Product findProduct = productRepository.findById(product.getId()).get();

        assertNotNull(cart,"카트는 Null이 아니여야 합니다.");
        assertEquals(findProduct.getQuantity(), 90);
    }

    /* 특정 회원의 카트에 담긴 상품들 가져오기 */
    @Test
    @Transactional
    void findByMemberCart(){
        createCartTest();

        List<Cart> byMemberId = cartRepository.findByMemberId(member.getId());

        assertNotEmpty(byMemberId, "Size가 0이 되어서는 안된다.");
        assertEquals(byMemberId.size(), 1);

    }

    @Test
    @Transactional
    void deleteCartTest(){
        createCartTest();

        List<Long> items = new ArrayList<>();
        items.add(1L);

        CartRequest.deleteItem deleteItems = CartRequest.deleteItem.builder()
                .cartIds(items)
                .build();

        for (Long cartId : deleteItems.getCartIds()) {
            cartRepository.deleteById(cartId);
        }

        List<Cart> byMemberId = cartRepository.findByMemberId(member.getId());

        assertEquals(byMemberId.size(), 0);
    }
}

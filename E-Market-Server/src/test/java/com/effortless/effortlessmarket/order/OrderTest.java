package com.effortless.effortlessmarket.order;

import com.effortless.effortlessmarket.domain.cart.dto.CartRequest;
import com.effortless.effortlessmarket.domain.cart.entity.Cart;
import com.effortless.effortlessmarket.domain.cart.service.CartService;
import com.effortless.effortlessmarket.domain.category.dto.CategoryRequest;
import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.service.CategoryService;
import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.entity.GenderType;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.repository.MemberRepository;
import com.effortless.effortlessmarket.domain.member.service.MemberService;
import com.effortless.effortlessmarket.domain.order.dto.OrderRequest;
import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.domain.order.service.OrderService;
import com.effortless.effortlessmarket.domain.orderDetail.dto.OrderDetailRequest;
import com.effortless.effortlessmarket.domain.product.dto.ProductRequest;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.product.repository.ProductRepository;
import com.effortless.effortlessmarket.domain.product.service.ProductService;
import com.effortless.effortlessmarket.domain.seller.dto.SellerRequest;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.domain.seller.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderTest {

    @Autowired private SellerService sellerService;
    @Autowired private CategoryService categoryService;
    @Autowired private ProductService productService;
    @Autowired private MemberService memberService;
    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;

    @Autowired private ProductRepository productRepository;
    @Autowired private MemberRepository memberRepository;


    private SellerRequest seller;
    private Product product;
    private Product product2;
    private CategoryRequest category;

    private Member member;

    @BeforeEach
    void setup(){
        /* 판매자 가입 */
        seller = SellerRequest.builder()
                .name("TestSeller")
                .email("email@gmail.com")
                .password("1234")
                .brandName("전자상가")
                .phoneNumber("010000000")
                .build();

        Seller newSeller = sellerService.createSeller(seller);

        /* 카테고리 등록 */
        category = CategoryRequest.builder()
                .name("키보드")
                .build();

        Category newCategory = categoryService.createCategory(category);

        /* 상품 등록 */
//        ProductRequest productRequest = ProductRequest.builder()
//                .name("한성 키보드")
//                .description("저렴해요")
//                .price(20000)
//                .quantity(100)
//                .sellerId(newSeller.getId())
//                .categoryId(newCategory.getId())
//                .build();
//
//        product = productService.createProduct(productRequest);
//
//        ProductRequest productRequest2 = ProductRequest.builder()
//                .name("커세어 키보드")
//                .description("저렴해요")
//                .price(20000)
//                .quantity(100)
//                .sellerId(newSeller.getId())
//                .categoryId(newCategory.getId())
//                .build();
//
//        product2 = productService.createProduct(productRequest2);

        /* 사용자 생성 */
        MemberRequest memberRequest = MemberRequest.builder()
                .name("테스터")
                .phoneNumber("0100000000")
                .email("tester01@gmail.com")
                .gender(GenderType.F)
                .password("password")
                .build();

        member = memberService.saveMember(memberRequest);
    }




    @Test @DisplayName("카트에 담긴 상품 주문 생성 테스트")
    void createCartInOrder_Success() {

        CartRequest.addItem cart1 = CartRequest.addItem.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .count(20)
                .build();

        CartRequest.addItem cart2 = CartRequest.addItem.builder()
                .memberId(member.getId())
                .productId(product2.getId())
                .count(20)
                .build();

        cartService.addCart(cart1);
        cartService.addCart(cart2);




        OrderRequest.FromCart fromCartRequest = OrderRequest.FromCart.builder()
                .memberId(member.getId())
                .cartIds(Arrays.asList(1L, 2L))
                .cardName("Test Card")
                .cardNumber("1234-5678-9012-3456")
                .build();

        Order resultOrder = orderService.createCartInOrder(fromCartRequest);

        assertNotNull(resultOrder);
        /* 총 두개의 orderDetail이 생성 되어야 한다.*/
        assertEquals(resultOrder.getOrderDetails().size(), 2);

        /* 주문을 성공시 카트의 상품은 비어 져야 한다. */
        List<Cart> allCartItems = cartService.getAllCartItems(member.getId());
        assertEquals(allCartItems.size(), 0);
    }



    @Test @DisplayName("바로 주문 생성 테스트")
    @Transactional
    void createOrder_Success() {

        OrderDetailRequest orderDetail = OrderDetailRequest.builder()
                .productId(product.getId())
                .count(2)
                .build();

        List<OrderDetailRequest> details = new ArrayList<>();
        details.add(orderDetail);

        OrderRequest.Order testOrder = OrderRequest.Order.builder()
                .memberId(member.getId())
                .cardName("Test Card")
                .cardNumber("1234-5678-9012-3456")
                .orderList(details)
                .build();



//        Order resultOrder = orderService.createOrder(testOrder);
//
//        assertNotNull(resultOrder);
//        assertEquals(1, resultOrder.getOrderDetails().size());
    }

}

package com.effortless.effortlessmarket.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomException {

    /* 회원 */
    MEMBER_DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    MEMBER_ALREADY_DELETE(HttpStatus.BAD_REQUEST, "이미 탈퇴 된 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 회원입니다."),

    /* 회원 주소 */
    MEMBER_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "이미 삭제 되었는 주소입니다."),

    /* 카테 고리*/
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 카테고리 입니다."),
    CATEGORY_IS_ALREADY(HttpStatus.CONFLICT, "이미 생성된 카테고리 입니다."),

    /* 판매자*/
    SELLER_DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 판매자 입니다."),

    /* 상품*/
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 상품입니다."),
    PRODUCT_COUNT_SHORTAGE(HttpStatus.BAD_REQUEST, "현재 남아 있는 재고가 없습니다."),

    /* 카트 */
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "카트에 담긴 상품을 찾을 수 없습니다."),

    /* 주문 */
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 주문 번호 입니다." );

    private final HttpStatus httpStatus;
    private final String message;

    CustomException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}

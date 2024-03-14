package com.effortless.effortlessmarket.domain.seller.service;

import com.effortless.effortlessmarket.domain.seller.dto.SellerRequest;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.domain.seller.repository.SellerRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    /* 판매자 생성 */
    public Seller createSeller(SellerRequest sellerRequest) {
        /* 판매자 이메일 검증 */
        sellerRepository.findByEmail(sellerRequest.getEmail())
                .ifPresent(s -> {
                    throw new CustomExceptionType(CustomException.SELLER_DUPLICATED_EMAIL);
                });

        /* 판매자 객체 생성 및 저장 */
        Seller seller = new Seller(); // Seller 객체를 새로 생성
        seller.saveSeller(sellerRequest);

        return sellerRepository.save(seller);
    }

    /* 판매자 수정 */
    public Seller updateSeller(SellerRequest sellerRequest){
        Seller findSeller = sellerRepository.findById(sellerRequest.getId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.SELLER_NOT_FOUND));

        findSeller.saveSeller(sellerRequest);
        return findSeller;
    }

    /* 판매자 삭제 */
    public String deleteSeller(Long id){
        Seller deletedMember =  sellerRepository.findById(id)
                .orElseThrow(()-> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND));

        sellerRepository.delete(deletedMember);
        return "정상적으로 탈퇴 되었습니다.";
    }


}

package com.effortless.effortlessmarket.domain.seller.serivce;


import com.effortless.effortlessmarket.domain.seller.dto.request.SellerRequest;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.domain.seller.repository.SellerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final SellerRepository sellerRepository;

    /* 판매자 생성 */
    @Transactional
    public ResponseEntity createSeller(SellerRequest request){
        Seller seller = sellerBuilder(request);
        sellerRepository.save(seller);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    /* 판매자 생성 - Builder */
    private Seller sellerBuilder(SellerRequest request) {
        return new Seller(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress()
        );
    }

    /* 판매자 조회 */
    public Seller findByIdToSeller(Long id){
        Seller seller = null;

        try{
            seller = sellerRepository.findById(id).get();
        } catch (Exception e){
            log.error(e.getMessage());
        }

        return seller;

    }


}

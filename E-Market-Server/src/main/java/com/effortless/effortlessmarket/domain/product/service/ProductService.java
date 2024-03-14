package com.effortless.effortlessmarket.domain.product.service;

import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.category.repository.CategoryRepository;
import com.effortless.effortlessmarket.domain.product.dto.ProductRequest;
import com.effortless.effortlessmarket.domain.product.dto.ProductResponse;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.product.repository.ProductRepository;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.domain.seller.repository.SellerRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    /* 상품 등록 */
    @Transactional
    public ProductResponse createProduct(ProductRequest request){
        /* 판매자 조회 */
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.SELLER_NOT_FOUND));

        /* 카테고리 조회 */
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.CATEGORY_NOT_FOUND));

        /* 상품 등록 */
        Product newProduct = new Product(request, category, seller);
        Product createdProduct = productRepository.save(newProduct);
        return new ProductResponse(createdProduct);

    }

    /* 상품 수정 */
    @Transactional
    public ProductResponse updateProduct(ProductRequest request){

        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.PRODUCT_NOT_FOUND));
        product.update(request);


        return new ProductResponse(product);
    }

    /* 상품 조회 */
    @Transactional
    public ProductResponse getProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new CustomExceptionType(CustomException.PRODUCT_NOT_FOUND));
        product.incrementViews();
        return new ProductResponse(product);
    }

    /* 상품 좋아요 */


    /* 상품 삭제 */
    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionType(CustomException.PRODUCT_NOT_FOUND));

        productRepository.delete(product);
    }

}

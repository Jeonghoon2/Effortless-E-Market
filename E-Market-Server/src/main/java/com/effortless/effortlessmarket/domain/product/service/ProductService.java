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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    @Value("${file.dir}")
    private String baseDir;

    /* 상품 등록 */
    @Transactional
    public ProductResponse createProduct(ProductRequest request) throws IOException {
        /* 판매자 조회 */
        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.SELLER_NOT_FOUND));

        /* 카테고리 조회 */
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.CATEGORY_NOT_FOUND));

        /* 이미지 등록 */
        String imgUrl;

        if (request.getFile().isEmpty()){
            imgUrl = "null";
        }else {
            imgUrl = saveImage(request.getFile());
        }


        /* 상품 등록 */
        Product newProduct = new Product(request, category, seller, imgUrl);
        Product createdProduct = productRepository.save(newProduct);
        return new ProductResponse(createdProduct);

    }

    /* 이미지 저장 */
    private String saveImage(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String storageFileName = UUID.randomUUID().toString() + fileExtension;
        String storagePath = baseDir + "/image/product/thumbnail/" + storageFileName;

        File file = new File(storagePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 상위 디렉토리가 없으면 생성
        }
        multipartFile.transferTo(file);
        return storagePath;
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

    /* Python Auto Work 용도 */
    public List<ProductResponse> getAllProductPy() {
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }


    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable).map(ProductResponse::new);
    }

    public Page<ProductResponse> getRecentProduct() {
        return productRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0,6, Sort.by("createdAt").descending())).map(ProductResponse::new);
    }
}

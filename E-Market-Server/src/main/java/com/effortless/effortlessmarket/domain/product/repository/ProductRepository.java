package com.effortless.effortlessmarket.domain.product.repository;


import com.effortless.effortlessmarket.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long>, ProductRepositoryCustom {
}

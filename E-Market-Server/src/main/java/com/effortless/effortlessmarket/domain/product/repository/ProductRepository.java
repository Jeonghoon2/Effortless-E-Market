package com.effortless.effortlessmarket.domain.product.repository;

import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.querydsl.core.group.GroupBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

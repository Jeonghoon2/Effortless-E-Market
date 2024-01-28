package com.effortless.effortlessmarket.domain.seller.repository;


import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long>, SellerRepositoryCustom {



}

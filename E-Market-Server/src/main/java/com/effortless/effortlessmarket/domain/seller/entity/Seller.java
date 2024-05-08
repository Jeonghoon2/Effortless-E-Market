package com.effortless.effortlessmarket.domain.seller.entity;


import com.effortless.effortlessmarket.domain.product.entity.Product;
import com.effortless.effortlessmarket.domain.seller.dto.SellerRequest;
import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "seller")
public class Seller extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "seller_email", length = 50)
    private String email;

    @Column(name = "seller_password", length = 200)
    private String password;

    @Column(name = "seller_name", length = 20)
    private String name;

    @Column(name = "seller_phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "seller_brand_name", length = 50)
    private String brandName;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> productList = new ArrayList<>();

    public Seller() {
    }

    public void saveSeller(SellerRequest sellerRequest) {
        this.email = sellerRequest.getEmail();
        this.password = sellerRequest.getPassword();
        this.name = sellerRequest.getName();
        this.phoneNumber = sellerRequest.getPhoneNumber();
        this.brandName = sellerRequest.getBrandName();
    }
}

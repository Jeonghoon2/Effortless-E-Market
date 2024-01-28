package com.effortless.effortlessmarket.domain.seller.entity;

import com.effortless.effortlessmarket.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_id")
    @Getter
    private Long id;

    @Column(name = "s_email")
    private String email;
    @Column(name = "s_password")
    private String password;
    @Column(name = "s_name")
    private String name;

    @Column(name = "s_phone_number")
    private String phoneNumber;

    @Column(name = "s_address")
    private String address;

    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();

    public Seller(String email, String password, String name, String phoneNumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}

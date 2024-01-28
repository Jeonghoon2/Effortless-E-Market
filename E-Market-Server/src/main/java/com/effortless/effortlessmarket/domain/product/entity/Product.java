package com.effortless.effortlessmarket.domain.product.entity;

import com.effortless.effortlessmarket.domain.image.entity.ProductImage;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "s_id")
    private Seller seller;

    @Column(name = "p_name")
    private String name;

    @Column(name = "p_price")
    private int price;

    @Column(name = "p_description")
    private String description;

    @Column(name = "p_quantity")
    private int quantity;

    @Column(name = "p_is_open")
    private int isOpen;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;

    public Product(Seller seller, String name, int price, String description, int quantity, int isOpen) {
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.isOpen = isOpen;
    }
}

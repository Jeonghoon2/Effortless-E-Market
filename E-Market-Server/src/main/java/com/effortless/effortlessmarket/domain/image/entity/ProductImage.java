package com.effortless.effortlessmarket.domain.image.entity;

import com.effortless.effortlessmarket.domain.image.rule.ProductImageRule;
import com.effortless.effortlessmarket.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pi_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "pi_rule")
    private ProductImageRule rule;

    @Column(name = "pi_location")
    private String location;

}

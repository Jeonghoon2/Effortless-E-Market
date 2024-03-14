package com.effortless.effortlessmarket.domain.product.entity;

import com.effortless.effortlessmarket.domain.category.entity.Category;
import com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail;
import com.effortless.effortlessmarket.domain.product.dto.ProductRequest;
import com.effortless.effortlessmarket.domain.seller.entity.Seller;
import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "product")
@ToString(of = {"name","price","description", "views"})
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", length = 50)
    private String name;

    @Column(name = "product_price")
    private Integer price;

    @Column(name = "product_description", length = 200)
    private String description;

    @Column(name = "product_quantity")
    private Integer quantity;

    @Column(name = "product_views")
    private Integer views = 0;

    @Column(name = "product_likes")
    private Integer likes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Product() {
    }

    public Product(ProductRequest request, Category category, Seller seller) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.quantity = request.getQuantity();
        this.seller = seller;
        this.category = category;
    }

    public void update(ProductRequest productRequest) {
        if (productRequest.getName() != null) {
            this.name = productRequest.getName();
        }
        if (productRequest.getPrice() != null) {
            this.price = productRequest.getPrice();
        }
        if (productRequest.getDescription() != null) {
            this.description = productRequest.getDescription();
        }
        if (productRequest.getQuantity() != null) {
            this.quantity = productRequest.getQuantity();
        }
    }

    public void decreaseCount(Integer count){
        this.quantity -= count;
    }

    public void increaseCount(Integer count){
        this.quantity += count;
    }

    public void incrementViews() {
        this.views++;
    }


}
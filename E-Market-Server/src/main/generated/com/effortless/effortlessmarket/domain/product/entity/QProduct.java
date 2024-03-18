package com.effortless.effortlessmarket.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = -1215523012L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.effortless.effortlessmarket.global.entity.QBaseTimeEntity _super = new com.effortless.effortlessmarket.global.entity.QBaseTimeEntity(this);

    public final com.effortless.effortlessmarket.domain.category.entity.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final ListPath<com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail, com.effortless.effortlessmarket.domain.orderDetail.entity.QOrderDetail> orderDetails = this.<com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail, com.effortless.effortlessmarket.domain.orderDetail.entity.QOrderDetail>createList("orderDetails", com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail.class, com.effortless.effortlessmarket.domain.orderDetail.entity.QOrderDetail.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final com.effortless.effortlessmarket.domain.seller.entity.QSeller seller;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.effortless.effortlessmarket.domain.category.entity.QCategory(forProperty("category"), inits.get("category")) : null;
        this.seller = inits.isInitialized("seller") ? new com.effortless.effortlessmarket.domain.seller.entity.QSeller(forProperty("seller")) : null;
    }

}


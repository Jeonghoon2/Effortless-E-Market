package com.effortless.effortlessmarket.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -1690682596L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final StringPath cardName = createString("cardName");

    public final StringPath cardNumber = createString("cardNumber");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.effortless.effortlessmarket.domain.member.entity.QMember member;

    public final com.effortless.effortlessmarket.domain.memberAddress.entity.QMemberAddress memberAddress;

    public final ListPath<com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail, com.effortless.effortlessmarket.domain.orderDetail.entity.QOrderDetail> orderDetails = this.<com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail, com.effortless.effortlessmarket.domain.orderDetail.entity.QOrderDetail>createList("orderDetails", com.effortless.effortlessmarket.domain.orderDetail.entity.OrderDetail.class, com.effortless.effortlessmarket.domain.orderDetail.entity.QOrderDetail.class, PathInits.DIRECT2);

    public final EnumPath<OrderStatus> status = createEnum("status", OrderStatus.class);

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.effortless.effortlessmarket.domain.member.entity.QMember(forProperty("member")) : null;
        this.memberAddress = inits.isInitialized("memberAddress") ? new com.effortless.effortlessmarket.domain.memberAddress.entity.QMemberAddress(forProperty("memberAddress"), inits.get("memberAddress")) : null;
    }

}


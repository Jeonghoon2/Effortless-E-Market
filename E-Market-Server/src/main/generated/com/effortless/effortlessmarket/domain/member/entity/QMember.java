package com.effortless.effortlessmarket.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 500402090L;

    public static final QMember member = new QMember("member1");

    public final com.effortless.effortlessmarket.global.entity.QBaseTimeEntity _super = new com.effortless.effortlessmarket.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final StringPath email = createString("email");

    public final EnumPath<GenderType> gender = createEnum("gender", GenderType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress, com.effortless.effortlessmarket.domain.memberAddress.entity.QMemberAddress> memberAddresses = this.<com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress, com.effortless.effortlessmarket.domain.memberAddress.entity.QMemberAddress>createList("memberAddresses", com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress.class, com.effortless.effortlessmarket.domain.memberAddress.entity.QMemberAddress.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final ListPath<com.effortless.effortlessmarket.domain.order.entity.Order, com.effortless.effortlessmarket.domain.order.entity.QOrder> orders = this.<com.effortless.effortlessmarket.domain.order.entity.Order, com.effortless.effortlessmarket.domain.order.entity.QOrder>createList("orders", com.effortless.effortlessmarket.domain.order.entity.Order.class, com.effortless.effortlessmarket.domain.order.entity.QOrder.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}


package com.effortless.effortlessmarket.domain.member.repository;

import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class MemberRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Optional<Member> saveMember(MemberRequest request) {
        return Optional.empty();
    }
}

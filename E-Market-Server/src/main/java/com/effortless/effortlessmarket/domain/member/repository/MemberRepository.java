package com.effortless.effortlessmarket.domain.member.repository;

import com.effortless.effortlessmarket.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {
}

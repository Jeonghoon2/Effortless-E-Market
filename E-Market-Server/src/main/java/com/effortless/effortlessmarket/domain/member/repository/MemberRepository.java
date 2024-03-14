package com.effortless.effortlessmarket.domain.member.repository;

import com.effortless.effortlessmarket.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberCustomRepository {

    Optional<Member> findByEmail(String email);




}

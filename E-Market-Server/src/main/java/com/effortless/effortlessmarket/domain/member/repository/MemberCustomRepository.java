package com.effortless.effortlessmarket.domain.member.repository;

import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.entity.Member;

import java.util.Optional;

public interface MemberCustomRepository {

    Optional<Member> saveMember(MemberRequest request);
}

package com.effortless.effortlessmarket.domain.member.service;

import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.dto.MemberResponse;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.repository.MemberRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /* 회원 생성 */
    @Transactional
    public Member saveMember(MemberRequest memberRequest) {

        /* 이미 가입된 이메일이 있을 경우 */
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
            throw new CustomExceptionType(CustomException.MEMBER_DUPLICATED_EMAIL);
        }

        Member member = new Member();

        member.save(memberRequest);

        return memberRepository.save(member);
    }

    @Transactional
    public MemberResponse updateMember(MemberRequest memberRequest) {

        /* 회원 검사 */
        Member findMember = memberRepository.findById(memberRequest.getId())
                .orElseThrow(() -> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND));

        findMember.save(memberRequest);

        MemberResponse member = new MemberResponse(findMember);

        return member;
    }


    /* 회원 삭제 */
    @Transactional
    public String deleteMember(Long id) {
        /* 없는 회원일 경우 */
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND));
        memberRepository.delete(existingMember);
        return "정상적으로 회원 탈퇴 되었습니다.";
    }

    /* 회원 조회 */
    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND));

        return new MemberResponse(member);
    }

    /* 모든 회원 조회 Py용*/
    public List<MemberResponse> getAllMemberPy() {
        List<MemberResponse> memberList = new ArrayList<>();

        List<Member> findAll = memberRepository.findAll();

        for (Member member : findAll) {
            MemberResponse newMember = new MemberResponse(member);
            memberList.add(newMember);
        }

        return memberList;

    }
}

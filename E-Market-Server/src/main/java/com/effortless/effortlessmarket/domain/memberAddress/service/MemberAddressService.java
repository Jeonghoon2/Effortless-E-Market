package com.effortless.effortlessmarket.domain.memberAddress.service;

import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.repository.MemberRepository;
import com.effortless.effortlessmarket.domain.memberAddress.dto.MemberAddressRequest;
import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import com.effortless.effortlessmarket.domain.memberAddress.respositroy.MemberAddressRepository;
import com.effortless.effortlessmarket.global.exception.CustomException;
import com.effortless.effortlessmarket.global.exception.CustomExceptionType;
import com.effortless.effortlessmarket.global.exception.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAddressService {

    private final MemberAddressRepository memberAddressRepository;
    private final MemberRepository memberRepository;

    /* 회원 주소 추가 */
    @Transactional
    public MemberAddress addMemberAddress(MemberAddressRequest memberAddressRequest) {
        Member member = memberRepository.findById(memberAddressRequest.getMemberId()).orElseThrow(
                ()-> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND)
        );

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.saveAddress(member, memberAddressRequest);

        member.getMemberAddresses().add(memberAddress);

        return memberAddressRepository.save(memberAddress);
    }

    /* 회원의 모든 주소 조회 */
    public List<MemberAddress> getAllAddress(Long memberId){
        return memberAddressRepository.findByMemberId(memberId);
    }

    /* 회원 주소 수정 */
    @Transactional
    public MemberAddress updateMemberAddress(MemberAddressRequest memberAddressRequest){
        Member member = memberRepository.findById(memberAddressRequest.getMemberId()).orElseThrow(
                () -> new CustomExceptionType(CustomException.MEMBER_NOT_FOUND)
        );

        MemberAddress memberAddress = memberAddressRepository.findById(memberAddressRequest.getId()).orElseThrow(
                () -> new CustomExceptionType(CustomException.MEMBER_ADDRESS_NOT_FOUND)
        );

        memberAddress.saveAddress(member, memberAddressRequest);

        return memberAddressRepository.save(memberAddress);
    }

    /* 회원 주소 삭제 */
    @Transactional
    public String deleteMemberAddress(Long id){
        MemberAddress memberAddress = memberAddressRepository.findById(id).orElseThrow(() -> new CustomExceptionType(CustomException.MEMBER_ADDRESS_NOT_FOUND));
        memberAddressRepository.delete(memberAddress);
        return "정상적으로 주소 삭제가 되었습니다.";

    }

}

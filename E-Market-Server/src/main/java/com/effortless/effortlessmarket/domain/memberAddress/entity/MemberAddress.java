package com.effortless.effortlessmarket.domain.memberAddress.entity;

import com.effortless.effortlessmarket.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 회원 별 배송지

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"recipientName","recipientPhoneNumber","recipientAddress","isDefault"})
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_id")
    private Member member;

    @Column(name = "ma_name")
    private String recipientName;

    @Column(name = "ma_phone_number")
    private String recipientPhoneNumber;

    @Column(name = "ma_address")
    private String recipientAddress;

    @Column(name = "ma_def")
    private Integer isDefault;

    public MemberAddress(String recipientName, String recipientPhoneNumber, String recipientAddress, Integer isDefault) {
        this.recipientName = recipientName;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.recipientAddress = recipientAddress;
        this.isDefault = isDefault;
    }




}

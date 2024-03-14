package com.effortless.effortlessmarket.domain.memberAddress.entity;

import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.memberAddress.dto.MemberAddressRequest;
import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_address")
public class MemberAddress extends BaseTimeEntity {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 20, nullable = false)
    private String recipientName;

    @Column(length = 15, nullable = false)
    private String recipientPhoneNumber;

    @Column(length = 100, nullable = false)
    private String recipientAddress;

    private boolean isDefault = false;


    public void saveAddress(Member member,MemberAddressRequest request) {
        this.member = member;
        if (request.getRecipientName() != null){
            this.recipientName = request.getRecipientName();
        }
        if (request.getRecipientPhoneNumber() != null){
            this.recipientPhoneNumber = request.getRecipientPhoneNumber();
        }
        if (request.getRecipientAddress() != null){
            this.recipientAddress = request.getRecipientAddress();
        }
    }

    public void updateDefaultAddress(){
        this.isDefault = true;
    }

}
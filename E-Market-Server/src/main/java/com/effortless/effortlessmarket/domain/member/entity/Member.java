package com.effortless.effortlessmarket.domain.member.entity;

import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import com.effortless.effortlessmarket.domain.order.entity.Order;
import com.effortless.effortlessmarket.global.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(of = {"email","name","gender","phoneNumber"})
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email", length = 50)
    private String email;

    @Column(name = "member_password", length = 200)
    private String password;

    @Column(name = "member_name", length = 20)
    private String name;

    @Column(name = "member_gender")
    @Enumerated(value = EnumType.STRING)
    private GenderType gender;

    @Column(name = "member_phone_number", length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<MemberAddress> memberAddresses = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member() {
    }

    public void save(MemberRequest memberRequest) {

        if (memberRequest.getEmail() != null) {
            this.email = memberRequest.getEmail();
        }
        if (memberRequest.getPassword() != null) {
            this.password = memberRequest.getPassword();
        }
        if (memberRequest.getName() != null) {
            this.name = memberRequest.getName();
        }
        if (memberRequest.getGender() != null) {
            this.gender = memberRequest.getGender();
        }
        if (memberRequest.getPhoneNumber() != null) {
            this.phoneNumber = memberRequest.getPhoneNumber();
        }
    }
}

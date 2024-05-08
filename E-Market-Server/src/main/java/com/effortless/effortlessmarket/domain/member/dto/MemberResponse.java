package com.effortless.effortlessmarket.domain.member.dto;

import com.effortless.effortlessmarket.domain.member.entity.GenderType;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private GenderType gender;
    private String phoneNumber;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.gender = member.getGender();
        this.phoneNumber = member.getPhoneNumber();
    }
}

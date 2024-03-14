package com.effortless.effortlessmarket.domain.member.dto;

import com.effortless.effortlessmarket.domain.member.entity.GenderType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRequest {
    private Long id;
    private String email;
    private String password;
    private String name;
    private GenderType gender;
    private String phoneNumber;

}

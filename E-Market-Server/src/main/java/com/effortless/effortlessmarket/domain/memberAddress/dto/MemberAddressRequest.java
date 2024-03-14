package com.effortless.effortlessmarket.domain.memberAddress.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberAddressRequest {

    private Long id;
    private Long memberId;
    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientAddress;
    private boolean isDefault;
}

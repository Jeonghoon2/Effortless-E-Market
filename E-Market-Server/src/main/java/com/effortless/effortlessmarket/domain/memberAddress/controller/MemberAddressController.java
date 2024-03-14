package com.effortless.effortlessmarket.domain.memberAddress.controller;

import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.memberAddress.dto.MemberAddressRequest;
import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import com.effortless.effortlessmarket.domain.memberAddress.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberAddressController {

    private final MemberAddressService memberAddressService;

    @PostMapping("/address")
    public ResponseEntity<MemberAddress> addMemberAddress(
            @RequestBody MemberAddressRequest memberAddressRequest
            ){
        MemberAddress saveMemberAddress = memberAddressService.addMemberAddress(memberAddressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveMemberAddress);
    }

    @GetMapping("/address/{memberId}")
    public ResponseEntity<List<MemberAddress>> getAllMemberAddress(@PathVariable("memberId") Long memberId){
        List<MemberAddress> allAddress = memberAddressService.getAllAddress(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(allAddress);
    }

    @PutMapping("/address")
    public ResponseEntity<MemberAddress> updateMemberAddress(
            @RequestBody MemberAddressRequest memberAddressRequest
    ){
        MemberAddress updateMemberAddress = memberAddressService.updateMemberAddress(memberAddressRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updateMemberAddress);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Object> deleteMemberAddress(@PathVariable Long addressId){
        String msg = memberAddressService.deleteMemberAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }


}

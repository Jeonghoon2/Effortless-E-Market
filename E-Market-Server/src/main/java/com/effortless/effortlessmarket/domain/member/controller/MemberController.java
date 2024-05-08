package com.effortless.effortlessmarket.domain.member.controller;


import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.dto.MemberResponse;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.service.MemberService;
import com.effortless.effortlessmarket.global.annotaion.ExcludeLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /* 회원 생성 */
    @PostMapping
    public ResponseEntity<Member> saveMember(
            @RequestBody MemberRequest memberRequest
    ){
        Member savedMember = memberService.saveMember(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    /* 회원 정보 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable("id") Long id){
        MemberResponse member = memberService.getMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    /* 모든 회원 조회 */
    @GetMapping("/all/py")
    public ResponseEntity<List<MemberResponse>> getAllMemberPy(){
        List<MemberResponse> memberList = memberService.getAllMemberPy();
        return ResponseEntity.ok(memberList);
    }

    /* 회원 수정 */
    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(
            @RequestBody MemberRequest memberRequest
    ){
        MemberResponse updatedMember = memberService.updateMember(memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    /* 회원 삭제*/
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(
            @PathVariable Long id
    ){
        String message = memberService.deleteMember(id);
        return ResponseEntity.ok(message);
    }
}

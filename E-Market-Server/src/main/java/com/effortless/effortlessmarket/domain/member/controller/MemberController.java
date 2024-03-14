package com.effortless.effortlessmarket.domain.member.controller;


import com.effortless.effortlessmarket.domain.member.dto.MemberRequest;
import com.effortless.effortlessmarket.domain.member.entity.Member;
import com.effortless.effortlessmarket.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /* 회원 수정 */
    @PutMapping
    public ResponseEntity<Member> updateMember(
            @RequestBody MemberRequest memberRequest
    ){
        Member updatedMember = memberService.updateMember(memberRequest);
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

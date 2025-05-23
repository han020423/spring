package com.example.study1.domain.member.controller;


import com.example.study1.domain.member.dto.*;
import com.example.study1.domain.member.service.MemberService;
import com.example.study1.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignRequestDto signRequestDto) {
        memberService.signUp(signRequestDto);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/sign-in")
//    public ResponseEntity<String> signIn(@RequestBody SignInRequestDto signInRequestDto) {
//        return ResponseEntity.ok(memberService.signIn(signInRequestDto));
//
//    }

    // 회원정보 수정 (비밀번호)
    @PatchMapping("/update")
    public ResponseEntity<Void> updateMember(@RequestBody MemberUpdateDto updateDto) {
        memberService.updateMember(updateDto);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMember(@RequestBody MemberDeleteDto deleteDto) {
        memberService.deleteMember(deleteDto);
        return ResponseEntity.ok().build();
    }

    //정보 조회
    @GetMapping("/me")
    public ResponseEntity<MemberProfileDto> getMyProfile(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        MemberProfileDto profile = memberService.getProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }
}

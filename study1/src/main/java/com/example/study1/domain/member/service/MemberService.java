package com.example.study1.domain.member.service;

import com.example.study1.domain.member.dto.*;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.global.exception.CustomException;
import com.example.study1.global.exception.ErrorCode;
import com.example.study1.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    //회원가입
    public void signUp(SignRequestDto signRequestDto){
        boolean isEmailDuplicate = memberRepository.existsByEmail(signRequestDto.email());

        if((isEmailDuplicate)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .username(signRequestDto.username())
                .password(signRequestDto.password())
                .email(signRequestDto.email())
                .build();//리포에 저장

        memberRepository.save(member);
    }

//    public String signIn(SignInRequestDto signInRequestDto) {
//        boolean existMember = memberRepository.existsByEmail(signInRequestDto.email());
//
//        if(!existMember) {
//            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
//        }
//
//        boolean checkPassword = memberRepository.existsByPassword(signInRequestDto.password());
//
//        if(!checkPassword) {
//            throw new CustomException(ErrorCode.INVALID_PASSWORD);
//        }
//        return "로그인 성공";
//    }
//    public Map<String, String> signIn(SignInRequestDto signInRequestDto) {
//        Member member = memberRepository.findByEmail(signInRequestDto.email())
//                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//
//        if(!member.getPassword().equals(signInRequestDto.password())) {
//            throw new CustomException(ErrorCode.INVALID_PASSWORD);
//        }
//
//        String accessToken = jwtUtil.generateAccessToken(member.getEmail());
//        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());
//
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("access_token", accessToken);
//        tokens.put("refresh_token", refreshToken);
//
//        return tokens;
//    }
//
//    public String refreshToken(String refreshToken) {
//        if(!jwtUtil.validateToken(refreshToken)) {
//            throw new CustomException(ErrorCode.INVALID_TOKEN);
//        }
//
//        String email = jwtUtil.extractEmail(refreshToken);
//        return jwtUtil.generateRefreshToken(email);
//    }

    public void updateMember(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberUpdateDto.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(memberUpdateDto.newPassword() == null) {
            throw new CustomException(ErrorCode.BLANK_PASSWORD);
        }

        if (!member.getPassword().equals(memberUpdateDto.currentPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        member.updatePassword(memberUpdateDto.newPassword());
        memberRepository.save(member);
    }

    public void deleteMember(MemberDeleteDto memberDeleteDto) {
        Member member = memberRepository.findById(memberDeleteDto.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getPassword().equals(memberDeleteDto.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        memberRepository.delete(member);
    }

    public MemberProfileDto getProfileByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberProfileDto.builder()
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .build();
    }
}

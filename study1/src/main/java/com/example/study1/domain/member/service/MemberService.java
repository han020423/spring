package com.example.study1.domain.member.service;

import com.example.study1.domain.member.dto.MemberDeleteDto;
import com.example.study1.domain.member.dto.MemberUpdateDto;
import com.example.study1.domain.member.dto.SignInRequestDto;
import com.example.study1.domain.member.dto.SignRequestDto;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;


    //회원가입
    public void signUp(SignRequestDto signRequestDto){
        boolean isEmailDuplicate = memberRepository.existsByEmail(signRequestDto.email());

        if((isEmailDuplicate)) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        Member member = Member.builder()
                .username(signRequestDto.username())
                .password(signRequestDto.password())
                .email(signRequestDto.email())
                .build();//리포에 저장

        memberRepository.save(member);
    }

    public String signIn(SignInRequestDto signInRequestDto) {
        boolean existMember = memberRepository.existsByEmail(signInRequestDto.email());

        if(!existMember) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다");
        }

        boolean checkPassword = memberRepository.existsByPassword(signInRequestDto.password());

        if(!checkPassword) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다");
        }
        return "로그인 성공";
    }

    public void updateMember(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberUpdateDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if(memberUpdateDto.newPassword() == null) {
            throw new IllegalArgumentException("비밀번호는 공백일 수 없습니다");
        }

        if (!member.getPassword().equals(memberUpdateDto.currentPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        member.updatePassword(memberUpdateDto.newPassword());
        memberRepository.save(member);
    }

    public void deleteMember(MemberDeleteDto memberDeleteDto) {
        Member member = memberRepository.findById(memberDeleteDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!member.getPassword().equals(memberDeleteDto.password())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }
}

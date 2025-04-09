package com.example.study1.domain.member.service;


import com.example.study1.domain.member.dto.MemberDeleteDto;
import com.example.study1.domain.member.dto.MemberUpdateDto;
import com.example.study1.domain.member.dto.SignInRequestDto;
import com.example.study1.domain.member.dto.SignRequestDto;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;


    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("사용자로부터 받은 정보를 통해 회원가입을 진행한다")
    @Test
    void signUp() {
        //given
        SignRequestDto signRequestDto = SignRequestDto.builder()
                .username("Test")
                .password("Testt")
                .email("Test@tst.com")
                .build();

        //when
        memberService.signUp(signRequestDto);

        //then

        Long memberId = 1L;
        Optional<Member> member = memberRepository.findById(memberId);

        assertThat(member).isNotNull();
    }

    @DisplayName("이미 존재하는 이메일로 회원가입을 시도하면 예외가 발생한다")
    @Test
    void signUpUpThrowExceptionCauseByEmail() {

        Member member = Member.builder()
                .username("Test")
                .password("Testt")
                .email("Test@tst.com")
                .build();

        memberRepository.save(member);

        SignRequestDto signUpRequestDto = SignRequestDto.builder()
                .username("Test")
                .password("Testt")
                .email("Test@tst.com")
                .build();

        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일 입니다.");
    }

    @DisplayName("사용자로부터 정보를 입력받아 로그인 진행한다")
    @Test
    void signIn() {
        Member member = Member.builder()
                .username("Test")
                .password("Testt")
                .email("Test@tst.com")
                .build();

        memberRepository.save(member);


        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .password("Testt")
                .email("Test@tst.com")
                .build();


        String result = memberService.signIn(signInRequestDto);
        assertThat(result).isEqualTo("로그인 성공");
    }

    @DisplayName("존재하지 않는 이메일을 받아오면 예외가 발생한다.")
    @Test
    void signInThrowExceptionCauseByEmail() {
        Member member = Member.builder()
                .username("Test")
                .password("Testt")
                .email("Test@tst.com")
                .build();

        memberRepository.save(member);


        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
                .password("Testt")
                .email("Test@tssst.com")
                .build();

        assertThatThrownBy(() -> memberService.signIn(signInRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 이메일입니다");
    }
    @DisplayName("사용자로부터 새로운 비밀번호를 입력받아 회원정보를 수정한다")
    @Test
    void updateMember() {
        //given
        Member member = Member.builder()
                .username("Test")
                .password("Testt")
                .email("Test@tst.com")
                .build();
        memberRepository.save(member);

        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .memberId(member.getId())
                .currentPassword("Testt")
                .newPassword("Testtdd")
                .build();
        //when

        memberService.updateMember(memberUpdateDto);


        //then
        Member updatedMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        assertThat(updatedMember.getPassword()).isEqualTo("Testtdd");
    }

    @DisplayName("회원 탈퇴가 정상적으로 동작한다.")
    @Test
    void deleteMember() {
        //given
        Member member = Member.builder()
                .username("Test")
                .password("1234")
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        MemberDeleteDto memberDeleteDto = MemberDeleteDto.builder()
                .memberId(member.getId())
                .password("1234")
                .build();
        //when
        memberService.deleteMember(memberDeleteDto);

        //then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }


}
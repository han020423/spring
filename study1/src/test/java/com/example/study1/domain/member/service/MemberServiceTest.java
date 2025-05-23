package com.example.study1.domain.member.service;


import com.example.study1.domain.member.dto.*;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.global.exception.CustomException;
import com.example.study1.global.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        Member saved = memberRepository.findAll().get(0);
        assertThat(saved.getEmail()).isEqualTo("Test@tst.com");

        assertThat(saved).isNotNull();
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

        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.signUp(signUpRequestDto);
        });
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

//    @DisplayName("사용자로부터 정보를 입력받아 로그인 진행한다")
//    @Test
//    void signIn() {
//        Member member = Member.builder()
//                .username("Test")
//                .password("Testt")
//                .email("Test@tst.com")
//                .build();
//
//        memberRepository.save(member);
//
//
//        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
//                .password("Testt")
//                .email("Test@tst.com")
//                .build();
//
//
//        String result = memberService.signIn(signInRequestDto);
//        assertThat(result).isEqualTo("로그인 성공");
//    }
//
//    @DisplayName("존재하지 않는 이메일을 받아오면 예외가 발생한다.")
//    @Test
//    void signInThrowExceptionCauseByEmail() {
//        Member member = Member.builder()
//                .username("Test")
//                .password("Testt")
//                .email("Test@tst.com")
//                .build();
//
//        memberRepository.save(member);
//
//
//        SignInRequestDto signInRequestDto = SignInRequestDto.builder()
//                .password("Testt")
//                .email("Test@tssst.com")
//                .build();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            memberService.signIn(signInRequestDto);
//        });
//        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
//    }
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

    @DisplayName("회원정보 조회")
    @Test
    void getProfileByEmail() {
        // given
        Member member = Member.builder()
                .username("홍길동")
                .email("test@example.com")
                .password("1234")
                .build();
        memberRepository.save(member);

        // when
        MemberProfileDto result = memberService.getProfileByEmail("test@example.com");

        // then
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.createdAt()).isNotNull();
    }

}
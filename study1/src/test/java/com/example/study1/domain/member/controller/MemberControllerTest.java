package com.example.study1.domain.member.controller;

import com.example.study1.domain.member.dto.MemberDeleteDto;
import com.example.study1.domain.member.dto.MemberUpdateDto;
import com.example.study1.domain.member.dto.SignInRequestDto;
import com.example.study1.domain.member.dto.SignRequestDto;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @DisplayName("회원가입 -성공")
    @Test
    void signUp() throws Exception {
        // given
        SignRequestDto request = SignRequestDto.builder()
                .username("홍길동")
                .email("hong@test.com")
                .password("1234")
                .build();

        // when & then
        mockMvc.perform(post("/api/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @DisplayName("로그인")
    @Test
    void signIn() throws Exception {
        // given
        SignRequestDto signUpDto = SignRequestDto.builder()
                .username("홍길동")
                .email("hong@test.com")
                .password("1234")
                .build();
        memberService.signUp(signUpDto);

        // when: 로그인 요청 생성
        SignInRequestDto request = SignInRequestDto.builder()
                .email("hong@test.com")
                .password("1234")
                .build();

        // then: 로그인 성공
        mockMvc.perform(post("/api/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateMember() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .username("홍길동")
                .email("hong@test.com")
                .password("1234")
                .build());

        MemberUpdateDto request = MemberUpdateDto.builder()
                .memberId(member.getId())
                .currentPassword("1234")
                .newPassword("5678")
                .build();

        // when & then
        mockMvc.perform(patch("/api/members/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMember() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .username("홍길동")
                .email("hong@test.com")
                .password("1234")
                .build());

        MemberDeleteDto request = MemberDeleteDto.builder()
                .memberId(member.getId())
                .password("1234")
                .build();

        // when & then
        mockMvc.perform(delete("/api/members/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @DisplayName("회원가입 - 중복 이메일")
    @Test
    void signUp_duplicateEmail() throws Exception {
        memberService.signUp(SignRequestDto.builder()
                .username("홍길동")
                .email("hong@test.com")
                .password("1234")
                .build());

        SignRequestDto duplicate = SignRequestDto.builder()
                .username("다른사람")
                .email("hong@test.com")
                .password("4567")
                .build();

        mockMvc.perform(post("/api/members/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().is(409));  // 전역 예외 처리에 따라 다르게 조절
    }

    @DisplayName("로그인 - 비밀번호 오류")
    @Test
    void signIn_wrongPassword() throws Exception {
        memberService.signUp(SignRequestDto.builder()
                .username("홍길동")
                .email("hong@test.com")
                .password("1234")
                .build());

        SignInRequestDto request = SignInRequestDto.builder()
                .email("hong@test.com")
                .password("wrong")
                .build();

        mockMvc.perform(post("/api/members/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(401));  // 혹은 isUnauthorized(), 예외 코드에 따라
    }
}
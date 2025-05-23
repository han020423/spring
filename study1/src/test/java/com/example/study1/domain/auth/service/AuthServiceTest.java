package com.example.study1.domain.auth.service;

import com.example.study1.domain.auth.dto.LoginRequestDto;
import com.example.study1.domain.auth.dto.TokenResponseDto;
import com.example.study1.domain.auth.repository.RefreshTokenRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.global.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("로그린")
    @Test
    void login() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("1234")
                .username("테스트유저")
                .build();
        memberRepository.save(member);

        LoginRequestDto request = new LoginRequestDto("test@example.com", "1234");

        // when
        TokenResponseDto response = authService.login(request);

        // then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @DisplayName("토큰 갱신")
    @Test
    void refreshToken() {
        // given
        Member member = Member.builder()
                .email("test@example.com")
                .password("1234")
                .username("테스트유저")
                .build();
        memberRepository.save(member);
        String refreshToken = authService.login(
                new LoginRequestDto("test@example.com", "1234")
        ).getRefreshToken();

        // when
        String newAccessToken = authService.refreshToken(refreshToken);

        // then
        assertNotNull(newAccessToken);
    }
}
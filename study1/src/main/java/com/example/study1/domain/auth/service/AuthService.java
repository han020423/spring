package com.example.study1.domain.auth.service;


import com.example.study1.domain.auth.dto.LoginRequestDto;
import com.example.study1.domain.auth.dto.TokenResponseDto;
import com.example.study1.domain.auth.entity.RefreshToken;
import com.example.study1.domain.auth.repository.RefreshTokenRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.global.exception.CustomException;
import com.example.study1.global.exception.ErrorCode;
import com.example.study1.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponseDto login(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getPassword().equals(request.password())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.generateAccessToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

        refreshTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        existing -> {
                            existing.setToken(refreshToken);
                            refreshTokenRepository.save(existing);
                        },
                () -> {
                RefreshToken newToken = RefreshToken.builder()
                        .memberId(member.getId())
                        .token(refreshToken)
                        .build();
                refreshTokenRepository.save(newToken);
                }
                );

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public String refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken saved = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        String newaToken = jwtUtil.generateAccessToken(
                jwtUtil.extractEmail(saved.getToken())
        );
        return newaToken;
    }
}

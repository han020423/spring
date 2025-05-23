package com.example.study1.domain.auth.controller;


import com.example.study1.domain.auth.dto.LoginRequestDto;
import com.example.study1.domain.auth.dto.TokenResponseDto;
import com.example.study1.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "").trim();
        String newAccessToken = authService.refreshToken(token);
        return ResponseEntity.ok(newAccessToken);
    }

}

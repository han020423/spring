package com.example.study1.domain.member.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberProfileDto(
        String email,
        LocalDateTime createdAt
) {
}

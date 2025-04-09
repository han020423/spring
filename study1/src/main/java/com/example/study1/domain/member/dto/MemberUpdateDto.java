package com.example.study1.domain.member.dto;

import lombok.Builder;

@Builder
public record MemberUpdateDto(
        Long memberId,
        String currentPassword,
        String newPassword
) {}

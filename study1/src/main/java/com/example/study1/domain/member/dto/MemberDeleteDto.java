package com.example.study1.domain.member.dto;

import lombok.Builder;

@Builder
public record MemberDeleteDto(
        Long memberId,
        String password
) {}

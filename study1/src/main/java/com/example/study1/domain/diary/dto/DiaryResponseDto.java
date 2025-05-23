package com.example.study1.domain.diary.dto;

import java.time.LocalDateTime;

public record DiaryResponseDto(
        Long id,
        String title,
        String content,
        boolean isPublic,
        String authorEmail,
        LocalDateTime createdAt
) {
}

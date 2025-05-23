package com.example.study1.domain.diary.dto;

public record DiaryRequestDto(
        String title,
        String content,
        boolean isPublic
) {
}

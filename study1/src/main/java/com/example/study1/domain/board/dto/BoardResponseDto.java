package com.example.study1.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardResponseDto(
        Long id,
        String title,
        String content,
        int viewCount,
        String writer
) {
}

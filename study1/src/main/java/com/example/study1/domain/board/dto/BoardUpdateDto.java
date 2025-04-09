package com.example.study1.domain.board.dto;

import lombok.Builder;

@Builder
public record BoardUpdateDto(
        Long boardId,
        Long memberId,
        String title,
        String content

) { }

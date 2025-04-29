package com.example.study1.domain.comment.dto;


import lombok.Builder;

@Builder
public record CommentRequestDto(
        Long memberId,
        Long boardId,
        Long parentId,
        String content
) {
}

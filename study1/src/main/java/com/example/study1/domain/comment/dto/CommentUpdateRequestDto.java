package com.example.study1.domain.comment.dto;

import lombok.Builder;

@Builder
public record CommentUpdateRequestDto(
        Long commentId,
        Long memberId,
        String newContent
) {
}

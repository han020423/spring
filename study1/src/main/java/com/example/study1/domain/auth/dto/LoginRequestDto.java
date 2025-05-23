package com.example.study1.domain.auth.dto;

import lombok.Builder;


@Builder
public record LoginRequestDto(
        String email,
        String password
) {


}

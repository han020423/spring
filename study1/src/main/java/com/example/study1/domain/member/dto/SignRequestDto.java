package com.example.study1.domain.member.dto;

import lombok.Builder;

@Builder
public record SignRequestDto(String username, String password, String email) {

}

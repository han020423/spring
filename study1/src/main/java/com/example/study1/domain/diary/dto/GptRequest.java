package com.example.study1.domain.diary.dto;

import java.util.List;

public record GptRequest(
        String model,
        List<GptMessage> messages
) {
}

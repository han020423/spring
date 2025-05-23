package com.example.study1.domain.diary.dto;

import java.awt.*;
import java.util.List;

public record GptResponse(
        List<Choice> choices
) {
    public record Choice(GptMessage message) {}
}

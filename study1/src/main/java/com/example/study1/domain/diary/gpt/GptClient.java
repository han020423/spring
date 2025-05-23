package com.example.study1.domain.diary.gpt;


import com.example.study1.domain.diary.dto.GptMessage;
import com.example.study1.domain.diary.dto.GptRequest;
import com.example.study1.domain.diary.dto.GptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GptClient {

    @Value("${openai.api.key}")
    private String openAiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String askChatGPT(String promport) {
        GptRequest request = new GptRequest(
                "gpt-3.5-turbo",
                List.of(new GptMessage("user", promport))
        );

        return webClient.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GptResponse.class)
                .map(response -> response.choices().get(0).message().content())
                .block();
    }

}

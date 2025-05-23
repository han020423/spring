package com.example.study1.domain.diary.controller;

import com.example.study1.domain.diary.dto.DiaryRequestDto;
import com.example.study1.domain.diary.dto.DiaryResponseDto;
import com.example.study1.domain.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestHeader("Authorization") String token, @RequestBody DiaryRequestDto dto) {
        diaryService.createDiary(token.replace("Bearer ", ""), dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody DiaryRequestDto dto) {
        diaryService.updateDiary(id, token.replace("Bearer ", ""), dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        diaryService.deleteDiary(id, token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<DiaryResponseDto>> myDiaries(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(diaryService.getMyDiaries(token.replace("Bearer ", "")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponseDto> getDiary(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return ResponseEntity.ok(diaryService.getDiary(id, token.replace("Bearer ", "")));
    }

    @GetMapping("/public")
    public ResponseEntity<List<DiaryResponseDto>> getPublic(Pageable pageable) {
        return ResponseEntity.ok(diaryService.getPublicDiaries(pageable));
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<DiaryResponseDto>> searchPublic(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(diaryService.searchPublicDiaries(keyword, pageable));
    }
}

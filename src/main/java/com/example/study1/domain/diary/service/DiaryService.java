package com.example.study1.domain.diary.service;


import com.example.study1.domain.diary.dto.DiaryRequestDto;
import com.example.study1.domain.diary.dto.DiaryResponseDto;
import com.example.study1.domain.diary.entity.Diary;
import com.example.study1.domain.diary.repository.DiaryRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.global.exception.CustomException;
import com.example.study1.global.exception.ErrorCode;
import com.example.study1.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private Member getMemberFromToken(String token) {
        String email = jwtUtil.extractEmail(token);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public void createDiary(String token, DiaryRequestDto dto) {
        Member member = getMemberFromToken(token);
        Diary diary = Diary.builder()
                .title(dto.title())
                .content(dto.content())
                .isPublic(dto.isPublic())
                .author(member)
                .build();
        diaryRepository.save(diary);
    }

    public void updateDiary(Long id, String token, DiaryRequestDto dto) {
        Member member = getMemberFromToken(token);
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        if(!diary.getAuthor().equals(member)) {
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }

        diary.setTitle(dto.title());
        diary.setContent(dto.content());
        diary.setPublic(dto.isPublic());
    }

    public void deleteDiary(Long id, String token) {
        Member member = getMemberFromToken(token);
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        if(!diary.getAuthor().equals(member)) {
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }

        diaryRepository.delete(diary);
    }

    public List<DiaryResponseDto> getMyDiaries(String token) {
        Member member = getMemberFromToken(token);
        return diaryRepository.findByAuthor(member).stream()
                .map(d -> new DiaryResponseDto(d.getId(), d.getTitle(), d.getContent(),
                        d.isPublic(), d.getAuthor().getEmail(), d.getCreatedAt()))
                .toList();
    }

    public DiaryResponseDto getDiary(Long id, String token) {
        Member member = getMemberFromToken(token);
        Diary d = diaryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));

        if(!d.isPublic() && !d.getAuthor().equals(member)) {
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }
        return new DiaryResponseDto(d.getId(), d.getTitle(), d.getContent(), d.isPublic(), d.getAuthor().getEmail(), d.getCreatedAt());
    }

    public List<DiaryResponseDto> getPublicDiaries(Pageable pageable) {
        return diaryRepository.findByIsPublicTrue(pageable).stream()
                .map(d -> new DiaryResponseDto(d.getId(), d.getTitle(), d.getContent(), true, d.getAuthor().getEmail(), d.getCreatedAt()))
                .toList();

    }

    public List<DiaryResponseDto> searchPublicDiaries(String keyword, Pageable pageable) {
        List<Diary> titleMatches = diaryRepository.findByIsPublicTrueAndTitleContaining(keyword, pageable);
        List<Diary> contentMatches = diaryRepository.findByIsPublicTrueAndContentContaining(keyword, pageable);

        // 중복 제거
        List<Diary> combined = Stream.concat(titleMatches.stream(), contentMatches.stream())
                .distinct()
                .toList();

        return combined.stream()
                .map(d -> new DiaryResponseDto(d.getId(), d.getTitle(), d.getContent(), true, d.getAuthor().getEmail(), d.getCreatedAt()))
                .toList();
    }
}

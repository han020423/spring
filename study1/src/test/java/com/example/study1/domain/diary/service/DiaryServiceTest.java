package com.example.study1.domain.diary.service;

import com.example.study1.domain.diary.dto.DiaryRequestDto;
import com.example.study1.domain.diary.dto.DiaryResponseDto;
import com.example.study1.domain.diary.entity.Diary;
import com.example.study1.domain.diary.repository.DiaryRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import com.example.study1.global.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class DiaryServiceTest {

    @Autowired
    private DiaryService diaryService;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("user@example.com")
                .password("1234")
                .username("user")
                .build();
        memberRepository.save(member);

        token = jwtUtil.generateAccessToken(member.getEmail());
    }

    @Test
    void createDiary() {
        //given
        DiaryRequestDto dto  = new DiaryRequestDto("제목1", "내용1", true);

        //when
        diaryService.createDiary(token, dto);

        //then
        List<Diary> diaries = diaryRepository.findByAuthor(member);
        assertThat(diaries).hasSize(1);
        assertThat(diaries.getFirst().getTitle()).isEqualTo("제목1");
    }

    @Test
    void updateDiary() {
        // Given
        Diary diary = Diary.builder()
                .title("Old Title")
                .content("Old Content")
                .isPublic(false)
                .author(member)
                .build();
        diaryRepository.save(diary);
        DiaryRequestDto dto = new DiaryRequestDto("New Title", "New Content", true);

        // When
        diaryService.updateDiary(diary.getId(), token, dto);

        // Then
        Diary updated = diaryRepository.findById(diary.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(updated.isPublic()).isTrue();
    }

    @Test
    void deleteDiary() {
        // Given
        Diary diary = Diary.builder()
                .title("Delete Me")
                .content("Bye")
                .isPublic(true)
                .author(member)
                .build();
        diaryRepository.save(diary);

        // When
        diaryService.deleteDiary(diary.getId(), token);

        // Then
        assertThat(diaryRepository.findById(diary.getId())).isEmpty();
    }

    @Test
    void getMyDiaries() {
        // Given
        diaryRepository.save(Diary.builder().title("My Diary").content("Content").isPublic(true).author(member).build());

        // When
        List<DiaryResponseDto> diaries = diaryService.getMyDiaries(token);

        // Then
        assertThat(diaries).hasSize(1);
        assertThat(diaries.get(0).title()).isEqualTo("My Diary");
    }

    @Test
    void getDiary() {
        // Given
        Diary diary = Diary.builder()
                .title("Public Diary")
                .content("Content")
                .isPublic(true)
                .author(member)
                .build();
        diaryRepository.save(diary);

        // When
        DiaryResponseDto response = diaryService.getDiary(diary.getId(), token);

        // Then
        assertThat(response.title()).isEqualTo("Public Diary");
    }

    @Test
    void getPublicDiaries() {
        // given
        Diary publicDiary = Diary.builder()
                .title("공개 일기")
                .content("내용")
                .isPublic(true)
                .author(member)
                .build();
        diaryRepository.save(publicDiary);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<DiaryResponseDto> result = diaryService.getPublicDiaries(pageable);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("공개 일기");
    }

    @Test
    void searchPublicDiaries() {
        // given
        diaryRepository.save(Diary.builder()
                .title("검색 키워드 포함")
                .content("내용")
                .isPublic(true)
                .author(member)
                .build());
        diaryRepository.save(Diary.builder()
                .title("다른 제목")
                .content("검색 키워드 포함")
                .isPublic(true)
                .author(member)
                .build());
        diaryRepository.save(Diary.builder()
                .title("비공개 일기")
                .content("검색 키워드 포함")
                .isPublic(false)
                .author(member)
                .build());

        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<DiaryResponseDto> result = diaryService.searchPublicDiaries("키워드", pageable);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(d -> d.title().contains("키워드") || d.content().contains("키워드"));
    }

    @Test
    void generateDiaryByAI() {
        // given
        String prompt = "살려줘.";

        // when
        DiaryResponseDto response = diaryService.generateDiaryByAI(token, prompt);

        // print
        System.out.println("AI 생성 제목: " + response.title());
        System.out.println("AI 생성 내용: " + response.content());
        // then
        assertThat(response.content()).contains("오늘");

    }
}
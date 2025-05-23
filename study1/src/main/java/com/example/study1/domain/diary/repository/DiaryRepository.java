package com.example.study1.domain.diary.repository;

import com.example.study1.domain.diary.entity.Diary;
import com.example.study1.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByAuthor(Member author);
    List<Diary> findByIsPublicTrue(Pageable pageable);
//    List<Diary> findByIsPublicTrueAndTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    List<Diary> findByIsPublicTrueAndTitleContaining(String keyword, Pageable pageable);
    List<Diary> findByIsPublicTrueAndContentContaining(String keyword, Pageable pageable);

}

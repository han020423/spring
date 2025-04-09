package com.example.study1.domain.board.entity;


import com.example.study1.domain.member.entity.Member;
import com.example.study1.global.tamplate.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount;

    @ManyToOne
    @JoinColumn(name = "member id", nullable = false)
    private Member member;

    @Builder
    private Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.member = member;
    }

    public void updateView() {
        this.viewCount += 1;
    }
    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

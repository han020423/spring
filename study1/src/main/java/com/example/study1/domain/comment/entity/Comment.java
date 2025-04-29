package com.example.study1.domain.comment.entity;

import com.example.study1.domain.board.entity.Board;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.global.tamplate.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder
    private Comment(String content, Board board, Member member, Comment parent) {
        this.content = content;
        this.board = board;
        this.member = member;
        this.parent = parent;
    }


    public void updateContent(String newContent) {
        this.content = newContent;
    }
}

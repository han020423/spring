package com.example.study1.domain.comment.service;

import com.example.study1.domain.board.entity.Board;
import com.example.study1.domain.board.repository.BoardRepository;
import com.example.study1.domain.comment.dto.CommentRequestDto;
import com.example.study1.domain.comment.dto.CommentUpdateRequestDto;
import com.example.study1.domain.comment.entity.Comment;
import com.example.study1.domain.comment.repository.CommentRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

    }

    @DisplayName("사용자가 댓글을 작성한다.")
    @Test
    void createComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .memberId(member.getId())
                .boardId(board.getId())
                .content("댓글 내용")
                .parentId(null)
                .build();

        //when
        commentService.createComment(commentRequestDto);

        //then
        List<Comment> comments = commentRepository.findByBoardId(board.getId());
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getContent()).isEqualTo("댓글 내용");
        assertThat(comments.get(0).getParent()).isNull();
    }

    @DisplayName("사용자가 댓글을 작성한다.")
    @Test
    void createReComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));
        Comment comment = commentRepository.save(Comment.builder()
                .content("첫댓글")
                .board(board)
                .member(member)
                .build());


        CommentRequestDto replyDto = CommentRequestDto.builder()
                .boardId(board.getId())
                .memberId(member.getId())
                .content("대댓글")
                .parentId(comment.getId())
                .build();

        //when
        commentService.createComment(replyDto);

        //then
        List<Comment> comments = commentRepository.findByBoardId(board.getId());
        assertThat(comments).hasSize(2);

        Comment reply = comments.stream()
                .filter(c -> c.getParent() != null)
                .findFirst()
                .orElseThrow();
        assertThat(reply.getContent()).isEqualTo("대댓글");
        assertThat(reply.getParent().getId()).isEqualTo(comment.getId());
    }

    @DisplayName("사용자가 댓글을 수정하면 내용이 바뀐다.")
    @Test
    void updateComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));
        Comment comment = commentRepository.save(Comment.builder()
                .content("원래내용")
                .board(board)
                .member(member)
                .build());

        CommentUpdateRequestDto updateDto = CommentUpdateRequestDto.builder()
                .commentId(comment.getId())
                .memberId(member.getId())
                .newContent("바꾼내용")
                .build();

        //when
        commentService.updateComment(updateDto);

        //then
        Comment updatedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(updatedComment.getContent()).isEqualTo("바꾼내용");
    }

    @DisplayName("작성자가 댓글을 삭제하면 댓글이 삭제된다.")
    @Test
    void deleteComment() {
        //given
        Member member = memberRepository.save(createMember());
        Board board = boardRepository.save(createBoard(member));
        Comment comment = commentRepository.save(Comment.builder()
                .content("내용")
                .board(board)
                .member(member)
                .build());

        //when
        commentService.deleteComment(comment.getId(), member.getId());

        //then
        boolean exists = commentRepository.findById(comment.getId()).isPresent();
        assertThat(exists).isFalse();
    }

    private Member createMember() {
        return Member.builder()
                .username("user1")
                .password("pass")
                .email("user1@test.com")
                .build();
    }

    private Board createBoard(Member member) {
        return Board.builder()
                .title("테스트 게시글")
                .content("게시글 내용")
                .member(member)
                .build();
    }
}
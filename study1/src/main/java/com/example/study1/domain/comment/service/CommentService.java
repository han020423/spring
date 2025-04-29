package com.example.study1.domain.comment.service;


import com.example.study1.domain.board.entity.Board;
import com.example.study1.domain.board.repository.BoardRepository;
import com.example.study1.domain.comment.dto.CommentRequestDto;
import com.example.study1.domain.comment.dto.CommentUpdateRequestDto;
import com.example.study1.domain.comment.entity.Comment;
import com.example.study1.domain.comment.repository.CommentRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public void createComment(CommentRequestDto commentRequestDto) {
        Member member = memberRepository.findById(commentRequestDto.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board = boardRepository.findById(commentRequestDto.boardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        Comment parent = null;
        if(commentRequestDto.parentId() != null) {
            parent = commentRepository.findById(commentRequestDto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        }

        Comment comment = Comment.builder()
                .content(commentRequestDto.content())
                .board(board)
                .member(member)
                .parent(parent)
                .build();

        commentRepository.save(comment);

    }
//    @Transactional
    public void updateComment(CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findById(commentUpdateRequestDto.commentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if(!comment.getMember().getId().equals(commentUpdateRequestDto.memberId())) {
            throw new IllegalArgumentException("작성자가 다릅니다.");
        }

        if (commentUpdateRequestDto.newContent() == null) {
            throw new IllegalArgumentException("댓글은 공백일 수 없습니다.");
        }

        comment.updateContent(commentUpdateRequestDto.newContent());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if(!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}

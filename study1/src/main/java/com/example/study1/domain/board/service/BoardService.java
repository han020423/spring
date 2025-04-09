package com.example.study1.domain.board.service;

import com.example.study1.domain.board.dto.BoardCreateRequestDto;
import com.example.study1.domain.board.dto.BoardResponseDto;
import com.example.study1.domain.board.dto.BoardUpdateDto;
import com.example.study1.domain.board.entity.Board;
import com.example.study1.domain.board.repository.BoardRepository;
import com.example.study1.domain.member.dto.MemberUpdateDto;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository BoardRepository;
    private final BoardRepository boardRepository;

    public void createBoard(BoardCreateRequestDto boardCreateRequestDtobo) {
        Member member = memberRepository.findById(boardCreateRequestDtobo.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        isValidTitle(boardCreateRequestDtobo.title());
        isValidContent(boardCreateRequestDtobo.content());

        Board board = Board.builder()
                .member(member)
                .title(boardCreateRequestDtobo.title())
                .content(boardCreateRequestDtobo.content())
                .build();
        BoardRepository.save(board);
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        board.updateView();
        boardRepository.save(board);

        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .writer(board.getMember().getUsername())
                .build();
    }

    public void deleteBoard(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!board.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }

        boardRepository.delete(board);
    }

    public void updateBoard(BoardUpdateDto boardUpdateDto) {
        Board board = boardRepository.findById(boardUpdateDto.boardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        if (!board.getMember().getId().equals(boardUpdateDto.memberId())) {
            throw new IllegalArgumentException("게시물 수정 권한이 없습니다.");
        }

        isValidTitle(boardUpdateDto.title());
        isValidContent(boardUpdateDto.content());

        board.updateBoard(boardUpdateDto.title(), boardUpdateDto.content());
        boardRepository.save(board);
    }


    private void isValidTitle(String title) {
        if(title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
    }
    private void isValidContent(String content) {
        if(content == null || content.isEmpty()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
    }

}

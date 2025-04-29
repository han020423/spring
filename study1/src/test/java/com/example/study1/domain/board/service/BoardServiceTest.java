package com.example.study1.domain.board.service;

import com.example.study1.domain.board.dto.BoardCreateRequestDto;
import com.example.study1.domain.board.dto.BoardResponseDto;
import com.example.study1.domain.board.dto.BoardUpdateDto;
import com.example.study1.domain.board.entity.Board;
import com.example.study1.domain.board.repository.BoardRepository;
import com.example.study1.domain.member.entity.Member;
import com.example.study1.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

    }

    @DisplayName("사용자로부터 받은 요청 값을 바탕으로 게시물을 생성한다.")
    @Test
    void createBoard() {
        Member member = createMember();
        memberRepository.save(member);

        Long memberId = member.getId();
        String title = "게시물 제목";
        String content = "게시물 내용";

        BoardCreateRequestDto boardCreateRequestDto = BoardCreateRequestDto.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .build();


        boardService.createBoard(boardCreateRequestDto);

        Board savedBoard = boardRepository.findAll().get(0);

        assertThat(savedBoard)
                .extracting(Board::getTitle, Board::getContent)
                .containsExactly(title, content);
    }

    @DisplayName("게시물 id를 통해 게시물을 조회할 수 있다.")
    @Test
    void getBoard() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("조회 테스트")
                .content("내용 테스트")
                .member(member)
                .build();
        boardRepository.save(board);

        //when
        boardService.getBoard(board.getId());
        BoardResponseDto result = boardService.getBoard(board.getId());

        //then
        assertThat(result.title()).isEqualTo("조회 테스트");
        assertThat(result.content()).isEqualTo("내용 테스트");
        assertThat(result.writer()).isEqualTo(member.getUsername());
        assertThat(result.viewCount()).isEqualTo(2);
    }
    @DisplayName("게시긓 작성자가 요청시 게시글을 삭제한다.")
    @Test
    void deleteBoard() {
        //given
        Member member = Member.builder()
                .username("Test")
                .password("1234")
                .email("test@test.com")
                .build();
        memberRepository.save(member);

        Board board = Board.builder()
                .title("삭제 테스트")
                .content("삭제 내용")
                .member(member)
                .build();
        boardRepository.save(board);

        Long boardId = board.getId();
        Long memberId = member.getId();

        //when
        boardService.deleteBoard(boardId, memberId);

        //then
        boolean exists = boardRepository.findById(memberId).isPresent();
        Assertions.assertThat(exists).isFalse();
    }

    @DisplayName("게시물 id와 사용자id를 통해 게시물을 수정한다")
    @Test
    void updateBoard() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Board board = Board.builder()
                .member(member)
                .title("원래 제목")
                .content("원래 내용")
                .build();
        boardRepository.save(board);

        BoardUpdateDto boardUpdateDto = BoardUpdateDto.builder()
                .boardId(board.getId())
                .memberId(member.getId())
                .title("수정된 제목")
                .content("수정된 내용")
                .build();
        //when
        boardService.updateBoard(boardUpdateDto);

        //then
        Board newBoard = boardRepository.findById(board.getId()).orElseThrow();
        assertThat(newBoard.getTitle()).isEqualTo("수정된 제목");
        assertThat(newBoard.getContent()).isEqualTo("수정된 내용");
    }

    private Member createMember() {
        return Member.builder()
                .email("trrrr@tttt.com")
                .username("tttt")
                .password("aaaa")
                .build();
    }

}
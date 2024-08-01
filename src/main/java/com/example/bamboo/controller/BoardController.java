package com.example.bamboo.controller;

import com.example.bamboo.dto.request.board.CreateRequestBoard;
import com.example.bamboo.dto.request.board.UpdateRequestBoard;
import com.example.bamboo.dto.response.board.GetBoardDTO;
import com.example.bamboo.dto.response.board.GetBoardListDTO;
import com.example.bamboo.dto.response.board.GetSearchBoardList;
import com.example.bamboo.dto.response.board.GetUserBoardListDTO;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal String email, @RequestBody CreateRequestBoard board) {
        if (email == null || board.getTitle() == null || board.getContent() == null) {
            throw new CustomException("필수 정보가 누락되었습니다.", ErrorCode.POST_NOT_FOUND);
        }
        boardService.createBoard(email, board);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/{boardNumber}")
    public ResponseEntity<?> getBoard(@PathVariable("boardNumber") int boardNumber) {
        GetBoardDTO boardListDTO = boardService.getBoard(boardNumber);
        return ResponseEntity.ok().body(boardListDTO);

    }

    @GetMapping
    public ResponseEntity<?> getBoardList() {
        List<GetBoardListDTO> boardList = boardService.getBoardList();
        return ResponseEntity.ok(boardList);
    }

    @GetMapping("/top-3")
    public ResponseEntity<?> getTopThreeBoard() {
        List<GetBoardListDTO> boardList = boardService.getTopThreeBoard();
        return ResponseEntity.ok(boardList);
    }

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<?> updateBoard(@PathVariable("boardNumber") int boardNumber, @RequestBody UpdateRequestBoard requestBoard) {
        boardService.updateBoard(boardNumber, requestBoard);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal String email, @PathVariable("boardNumber") int boardNumber) {
        boardService.deleteBoard(email, boardNumber);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{keyword}/search")
    public ResponseEntity<?> searchBoardList(@PathVariable("keyword") String keyword) {
        List<GetSearchBoardList> board = boardService.searchBoardList(keyword);
        return ResponseEntity.ok().body(board);
    }


    @PatchMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<?> viewCount(@PathVariable("boardNumber") int boardNumber) {
        boardService.viewCount(boardNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}/user-board-list")
    public ResponseEntity<?> getUserBoardList(@PathVariable String email) {
        List<GetUserBoardListDTO> userBoardList = boardService.getUserBoardList(email);
        return ResponseEntity.ok().body(userBoardList);
    }
}

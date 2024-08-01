package com.example.bamboo.controller;

import com.example.bamboo.dto.request.comment.CommentDTO;
import com.example.bamboo.dto.response.comment.GetCommentListDTO;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardNumber}")
    public ResponseEntity<?> addComment(@PathVariable("boardNumber") int boardNumber, @AuthenticationPrincipal String email, @RequestBody CommentDTO commentDTO) {
        if (commentDTO.getContent().isEmpty()) {
            throw new CustomException("댓글 내용이 없습니다.", ErrorCode.COMMENT_NOT_FOUND);
        }
        commentService.addComment(boardNumber, email, commentDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{boardNumber}")
    public ResponseEntity<?> getCommentList(@PathVariable("boardNumber") int boardNumber, @AuthenticationPrincipal String email) {
        List<GetCommentListDTO> commentListDTOS = commentService.getCommentList(boardNumber, email);
        return ResponseEntity.ok().body(commentListDTOS);
    }

}

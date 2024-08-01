package com.example.bamboo.service;

import com.example.bamboo.dto.request.comment.CommentDTO;
import com.example.bamboo.dto.response.comment.GetCommentListDTO;
import com.example.bamboo.entity.BoardEntity;
import com.example.bamboo.entity.CommentEntity;
import com.example.bamboo.entity.UserEntity;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.repository.BoardImageRepository;
import com.example.bamboo.repository.BoardRepository;
import com.example.bamboo.repository.CommentRepository;
import com.example.bamboo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class  CommentService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public void addComment(int boardNumber, String email, CommentDTO commentDTO){
        if(boardNumber > 0 && email != null && commentDTO.getContent() != null){
            BoardEntity board = boardRepository.findById(boardNumber)
                    .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", ErrorCode.POST_NOT_FOUND));
            UserEntity user = userRepository.findByEmail(email);

            CommentEntity comment = CommentEntity.builder()
                    .createdAt(LocalDateTime.now())
                    .user(user)
                    .board(board)
                    .content(commentDTO.getContent())
                    .build();

            commentRepository.save(comment);
        }
    }

    public List<GetCommentListDTO> getCommentList(int boardNumber, String email){
        if(boardNumber > 0 && email != null){
            List<CommentEntity> comments = commentRepository.findByBoard_BoardId(boardNumber);

            List<GetCommentListDTO> commentListDTO = new ArrayList<>();
            for (CommentEntity comment : comments) {
                UserEntity commentUser = comment.getUser();

                GetCommentListDTO dto = GetCommentListDTO.builder()
                        .nickname(commentUser.getNickname())
                        .profileImg(commentUser.getProfileImg())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build();
                commentListDTO.add(dto);
            }

            return commentListDTO;
        }
        return null;
    }
}

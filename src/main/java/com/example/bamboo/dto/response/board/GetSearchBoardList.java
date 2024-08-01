package com.example.bamboo.dto.response.board;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetSearchBoardList {
    private int boardNumber;
    private String title;
    private String content;
    private String email;
    private String boardImage;
    private LocalDateTime createdAt;
    private String nickname;
    private String profileImg;
    private int views;
    private int favorite;
    private int commentCount;
}

package com.example.bamboo.dto.response.board;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class GetBoardListDTO {
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

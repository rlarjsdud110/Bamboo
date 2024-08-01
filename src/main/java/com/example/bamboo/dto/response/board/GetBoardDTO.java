package com.example.bamboo.dto.response.board;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class GetBoardDTO {
    private int boardNumber;
    private String title;
    private String content;
    private String email;
    private List<String> boardImageList;
    private LocalDateTime createdAt;
    private String nickname;
    private String profileImg;
    private int commentCount;
}

package com.example.bamboo.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCommentListDTO {
    private String nickname;
    private String profileImg;
    private LocalDateTime createdAt;
    private String content;
}

package com.example.bamboo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int views;
    private int favorite;
    private List<BoardImageDTO> boardImages;
}

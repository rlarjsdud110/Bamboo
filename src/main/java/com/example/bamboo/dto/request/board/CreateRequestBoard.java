package com.example.bamboo.dto.request.board;

import lombok.Data;

import java.util.List;

@Data
public class CreateRequestBoard {
    String title;
    String content;
    private List<String> boardImages;
}

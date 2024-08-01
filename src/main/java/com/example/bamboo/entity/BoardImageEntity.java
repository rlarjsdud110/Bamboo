package com.example.bamboo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "board_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    public BoardImageEntity(BoardEntity board, String image){
        this.imageUrl = image;
        this.board = board;
    }
}

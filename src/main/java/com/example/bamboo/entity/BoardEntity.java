package com.example.bamboo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private int boardId;

    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "views")
    private int views;
    @Column(name = "favorite")
    private int favorite;


    @ManyToOne
    @JoinColumn(name = "user_no")
    @JsonIgnore
    private UserEntity user;

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<BoardImageEntity> boardImages;

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<CommentEntity> comments;

}



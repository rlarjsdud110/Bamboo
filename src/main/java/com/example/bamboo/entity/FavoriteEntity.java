package com.example.bamboo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private int favoriteId;
    @ManyToOne
    @JoinColumn(name = "user_no")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;
}


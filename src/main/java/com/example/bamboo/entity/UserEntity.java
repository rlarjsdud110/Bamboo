package com.example.bamboo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private int userNo;

    @Column(name = "name")
    private String name;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "profile_img")
    private String profileImg;
    @Column(name = "role")
    private Integer role;
    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "user")
    private List<BoardEntity> boards;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;
}


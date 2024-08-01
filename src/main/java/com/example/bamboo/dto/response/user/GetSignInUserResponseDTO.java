package com.example.bamboo.dto.response.user;


import com.example.bamboo.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetSignInUserResponseDTO {
    private String nickname;
    private String email;
    private String profileImg;

    public GetSignInUserResponseDTO (UserEntity userEntity){
        this.nickname = userEntity.getNickname();
        this.email = userEntity.getEmail();
        this.profileImg = userEntity.getProfileImg();

    }
}

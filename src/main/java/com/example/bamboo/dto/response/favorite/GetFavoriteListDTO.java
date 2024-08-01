package com.example.bamboo.dto.response.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFavoriteListDTO {
    private String email;
    private String nickname;
    private String profileImg;
}

package com.example.bamboo.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCheckDTO {
    private String email;
    private String name;
}

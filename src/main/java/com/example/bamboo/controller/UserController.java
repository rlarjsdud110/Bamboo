package com.example.bamboo.controller;

import com.example.bamboo.dto.request.user.UpdateUserNickname;
import com.example.bamboo.dto.request.user.UpdateUserProfileImg;
import com.example.bamboo.dto.response.user.GetSignInUserResponseDTO;
import com.example.bamboo.dto.response.user.LoginUserDTO;
import com.example.bamboo.dto.UserDTO;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        if (userDTO == null || userDTO.getEmail() == null) {
            throw new CustomException("이메일 혹은 비밀번호가 틀렸습니다.", ErrorCode.NOT_USER_EMAIL_PASSWORD);
        }
        userService.create(userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        if (userDTO.getPassword().isEmpty() || userDTO.getEmail().isEmpty()) {
            throw new CustomException("이메일 혹은 비밀번호가 틀렸습니다.", ErrorCode.NOT_USER_EMAIL_PASSWORD);
        }
        LoginUserDTO user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword());

        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<?> getLoginUserInfo(@AuthenticationPrincipal String email) {
        GetSignInUserResponseDTO userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok().body(userInfo);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserInfo(@PathVariable String email) {
        GetSignInUserResponseDTO userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok().body(userInfo);

    }

    @PatchMapping("/nickname")
    public ResponseEntity<?> updateNickname(@AuthenticationPrincipal String email, @RequestBody UpdateUserNickname userNickname) {
        boolean result = userService.updateNickname(email, userNickname);
        if (result) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfileImg(@AuthenticationPrincipal String email, @RequestBody UpdateUserProfileImg userProfileImg) {
        userService.updateProfileImg(email, userProfileImg);

        return ResponseEntity.ok().build();
    }
}

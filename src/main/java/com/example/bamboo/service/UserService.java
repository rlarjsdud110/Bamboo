package com.example.bamboo.service;

import com.example.bamboo.config.jwt.TokenProvider;
import com.example.bamboo.dto.UserDTO;
import com.example.bamboo.dto.request.user.UpdateUserNickname;
import com.example.bamboo.dto.request.user.UpdateUserProfileImg;
import com.example.bamboo.dto.response.user.GetSignInUserResponseDTO;
import com.example.bamboo.dto.response.user.LoginUserDTO;
import com.example.bamboo.entity.PeopleEntity;
import com.example.bamboo.entity.UserEntity;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.repository.PeopleRepository;
import com.example.bamboo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PeopleRepository peopleRepository;
    private final TokenProvider tokenProvider;


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void create(UserDTO userDTO){
        if (userDTO == null || userDTO.getPassword() == null){
            log.warn("UserEmail or UserPassword Nullable");
            throw new CustomException("잘못된 회원가입 방식입니다.", ErrorCode.RETRY);
        }
        if(userRepository.existsByEmail(userDTO.getEmail())){
            log.warn("UserEmail already exists {}", userDTO.getEmail());
            throw new CustomException("중복된 이메일 입니다.", ErrorCode.DUPLICATE_EMAIL);
        }
        if(userRepository.existsByNickname(userDTO.getNickname())){
            log.warn("UserEmail already exists {}", userDTO.getNickname());
            throw new CustomException("중복된 닉네임 입니다.", ErrorCode.DUPLICATE_NICKNAME);
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        log.info("Encoded password: {}", encodedPassword);

        UserEntity user = UserEntity.builder()
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .nickname(userDTO.getNickname())
                .name(userDTO.getName())
                .status(1)
                .build();

        PeopleEntity people = peopleRepository.findByName(userDTO.getName()).get();
        people.setStatus(1);
        peopleRepository.save(people);

        userRepository.save(user);
    }

    public LoginUserDTO getByCredentials(String userEmail, String password){
        UserEntity user = userRepository.findByEmail(userEmail);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            String token = tokenProvider.create(user);
            LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .token(token).build();
            return loginUserDTO;
        }

        return null;
    }


    public GetSignInUserResponseDTO getUserInfo(String email){
        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            log.warn("User not found or authentication failed for email: {}", email);
            throw new CustomException("존재하지 않는 유저입니다.", ErrorCode.NOT_USER_INFO);
        }

        return new GetSignInUserResponseDTO(user);
    }

    public Boolean updateNickname(String email, UpdateUserNickname userNickname){
        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            log.warn("User not found or authentication failed for email: {}", email);
            throw new CustomException("유저를 찾을 수 없습니다. 다시 시도해주세요",ErrorCode.RETRY);
        }
        user.setNickname(userNickname.getNickname());
        userRepository.save(user);
        return true;
    }

    public void updateProfileImg(String email, UpdateUserProfileImg userProfileImg){
        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            log.warn("User not found or authentication failed for email: {}", email);
            throw new CustomException("유저를 찾을 수 없습니다. 다시 시도해주세요",ErrorCode.RETRY);
        }
        if(userProfileImg.getProfileImg() == null || userProfileImg.getProfileImg().isEmpty()){
            user.setProfileImg(null);
            userRepository.save(user);
        }
        user.setProfileImg(userProfileImg.getProfileImg());
        userRepository.save(user);
    }
}
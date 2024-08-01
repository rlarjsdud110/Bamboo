package com.example.bamboo.service;

import com.example.bamboo.dto.request.user.UserCheckDTO;
import com.example.bamboo.entity.PeopleEntity;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class OAuthUserService extends DefaultOAuth2UserService {

    private final PeopleRepository peopleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String username = (String) oAuth2User.getAttributes().get("name");
        String userEmail = (String) oAuth2User.getAttributes().get("email");

        PeopleEntity people = peopleRepository.findByName(username)
                .orElseThrow(()-> new CustomException("우리FISA 학생이 아니군요", ErrorCode.NOT_FISA_STUDENT));

        if (people.getStatus() == 0) {
            UserCheckDTO userCheckDTO = UserCheckDTO.builder()
                    .email(userEmail)
                    .name(username)
                    .build();
            throw new CustomException("회원가입이 필요합니다.", userCheckDTO, ErrorCode.SIGNUP_REQUIRED);
        }

        log.info("Successfully user info username{}",username);
        return oAuth2User;
    }

}

package com.example.bamboo.config.security;

import com.example.bamboo.config.jwt.TokenProvider;
import com.example.bamboo.entity.UserEntity;
import com.example.bamboo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = (String) oAuth2User.getAttributes().get("email");

        UserEntity user = userRepository.findByEmail(userEmail);
        String token = tokenProvider.create(user);

        String redirectUrl = "http://localhost:3000/auth?token=" + token;
        response.sendRedirect(redirectUrl);
        log.info("token : {}", token);
    }
}
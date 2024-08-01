package com.example.bamboo.config.jwt;

import com.example.bamboo.entity.UserEntity;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String create(UserEntity userEntity){
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .setSubject(userEntity.getEmail())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserEmail(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e){
            throw new CustomException("토큰 정보가 잘못됐거나 토큰이 만료되었습니다.", ErrorCode.TOKEN_NOT_FOUND);
        }
    }


}

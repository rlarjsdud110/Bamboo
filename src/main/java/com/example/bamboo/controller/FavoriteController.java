package com.example.bamboo.controller;

import com.example.bamboo.dto.response.favorite.GetFavoriteListDTO;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/{boardNumber}")
    public ResponseEntity<?> favorite(@AuthenticationPrincipal String email, @PathVariable("boardNumber") int boardNumber) {
        boolean result = favoriteService.favorite(email, boardNumber);
        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/{boardNumber}/list")
    public ResponseEntity<?> getFavoirteList(@PathVariable("boardNumber") int boardNumber) {
        List<GetFavoriteListDTO> favoriteListDTO = favoriteService.getFavoriteList(boardNumber);
        return ResponseEntity.ok().body(favoriteListDTO);
    }
}

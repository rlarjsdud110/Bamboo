package com.example.bamboo.service;

import com.example.bamboo.dto.response.favorite.GetFavoriteListDTO;
import com.example.bamboo.entity.BoardEntity;
import com.example.bamboo.entity.FavoriteEntity;
import com.example.bamboo.entity.UserEntity;
import com.example.bamboo.exception.CustomException;
import com.example.bamboo.exception.ErrorCode;
import com.example.bamboo.repository.BoardRepository;
import com.example.bamboo.repository.FavoriteRepository;
import com.example.bamboo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Boolean favorite(String email, int boardNumber) {
        UserEntity user = userRepository.findByEmail(email);

        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", ErrorCode.POST_NOT_FOUND));

        Optional<FavoriteEntity> favorite = favoriteRepository.findByUser_UserNoAndBoard_BoardId(user.getUserNo(), board.getBoardId());

        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            board.setFavorite(board.getFavorite() - 1);
            boardRepository.save(board);
            return false;
        } else {
            FavoriteEntity newFavorite = FavoriteEntity.builder()
                    .user(user)
                    .board(board)
                    .build();
            favoriteRepository.save(newFavorite);
            board.setFavorite(board.getFavorite() + 1);
            boardRepository.save(board);
            return true;
        }
    }

    @Transactional
    public List<GetFavoriteListDTO> getFavoriteList(int boardNumber) {
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", ErrorCode.POST_NOT_FOUND));

        List<FavoriteEntity> favorites = favoriteRepository.findByBoard_BoardId(boardNumber);

        List<GetFavoriteListDTO> favoriteList = new ArrayList<>();
        for (FavoriteEntity favorite : favorites) {
            UserEntity user = favorite.getUser();
            GetFavoriteListDTO dto = GetFavoriteListDTO.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .build();
            favoriteList.add(dto);
        }

        return favoriteList;
    }
}

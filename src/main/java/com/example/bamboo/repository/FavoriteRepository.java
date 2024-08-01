package com.example.bamboo.repository;

import com.example.bamboo.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Integer> {
    Optional<FavoriteEntity> findByUser_UserNoAndBoard_BoardId(int userNo, int boardId);
    List<FavoriteEntity> findByBoard_BoardId(int boardId);
}

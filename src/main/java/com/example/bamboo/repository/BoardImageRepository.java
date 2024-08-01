package com.example.bamboo.repository;

import com.example.bamboo.entity.BoardEntity;
import com.example.bamboo.entity.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Integer> {
    List<BoardImageEntity> findByBoard_BoardId(int boardId);
}

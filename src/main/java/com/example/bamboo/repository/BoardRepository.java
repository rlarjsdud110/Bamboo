package com.example.bamboo.repository;

import com.example.bamboo.entity.BoardEntity;
import com.example.bamboo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findByOrderByCreatedAtDesc();

    List<BoardEntity> findTop3ByOrderByViewsDesc();
    List<BoardEntity> findByTitleContaining(String keyword );

}
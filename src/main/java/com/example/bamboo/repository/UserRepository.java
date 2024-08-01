package com.example.bamboo.repository;


import com.example.bamboo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);
}

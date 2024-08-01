package com.example.bamboo.repository;

import com.example.bamboo.entity.PeopleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity, Integer> {
    Optional<PeopleEntity> findByName(String name);
}

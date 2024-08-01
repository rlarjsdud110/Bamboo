package com.example.bamboo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "people")
public class PeopleEntity {
    @Id
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private int status;
}
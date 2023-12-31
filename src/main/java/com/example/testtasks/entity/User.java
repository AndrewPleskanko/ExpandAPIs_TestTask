package com.example.testtasks.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "registration")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;
}


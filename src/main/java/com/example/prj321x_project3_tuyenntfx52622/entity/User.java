package com.example.prj321x_project3_tuyenntfx52622.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

    @Column(nullable = false, length = 60)
    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isLocked = false;

    private String lockReason;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Role {
        USER, DOCTOR, ADMIN
    }
}
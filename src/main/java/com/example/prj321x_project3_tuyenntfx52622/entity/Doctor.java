package com.example.prj321x_project3_tuyenntfx52622.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

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

    private String introduction;

    private String education;

    private String achievements;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isLocked = false;

    private String lockReason;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DOCTOR;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<DoctorSpecialty> specialties;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Role {
        USER, DOCTOR, ADMIN
    }
}

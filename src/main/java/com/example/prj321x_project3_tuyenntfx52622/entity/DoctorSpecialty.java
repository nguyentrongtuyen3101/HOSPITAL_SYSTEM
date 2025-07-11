package com.example.prj321x_project3_tuyenntfx52622.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_specialties")
public class DoctorSpecialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính đơn, tự tăng

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;
}
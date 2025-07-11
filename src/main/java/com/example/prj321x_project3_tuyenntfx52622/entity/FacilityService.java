package com.example.prj321x_project3_tuyenntfx52622.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facility_services")
public class FacilityService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private MedicalFacility facility;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private MedicalService service;

    @Column(nullable = false)
    private String cost;
}
package com.example.prj321x_project3_tuyenntfx52622.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specialties")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specialtyId;

    @Column(nullable = false)
    private String name;

    private String description;
    private int searchnumber=0;
    @OneToMany(mappedBy = "specialty", cascade = CascadeType.ALL)
    private List<DoctorSpecialty> doctors;
}

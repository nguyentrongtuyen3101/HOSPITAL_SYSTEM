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
    @EmbeddedId
    private DoctorSpecialtyId id;

    @ManyToOne
    @MapsId("doctorId")
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @MapsId("specialtyId")
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @Embeddable
    @Data
    public static class DoctorSpecialtyId implements java.io.Serializable {
        private Long doctorId;
        private Long specialtyId;
    }
}
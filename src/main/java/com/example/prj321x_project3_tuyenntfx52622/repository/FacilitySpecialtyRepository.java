package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.FacilitySpecialty;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalFacility;
import com.example.prj321x_project3_tuyenntfx52622.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilitySpecialtyRepository extends JpaRepository<FacilitySpecialty, Integer> {
    public boolean existsByFacilityAndSpecialty(MedicalFacility facility, Specialty specialty);
    public FacilitySpecialty findByFacility(MedicalFacility facility);
    public List<FacilitySpecialty> findAllByFacility(MedicalFacility facility);
}

package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.FacilityService;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalFacility;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityServiceRepository extends JpaRepository<FacilityService,Long> {
    public List<FacilityService> findFacilityServicesByFacility(MedicalFacility medicalFacility);
    public FacilityService findByFacilityAndService(MedicalFacility medicalFacility, MedicalService medicalService);
}

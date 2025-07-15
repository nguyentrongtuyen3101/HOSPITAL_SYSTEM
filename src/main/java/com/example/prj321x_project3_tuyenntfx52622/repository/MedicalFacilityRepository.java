package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalFacilityRepository extends JpaRepository<MedicalFacility,Long> {
    public boolean existsByName(String name);
    public boolean existsByAddress(String address);
    public List<MedicalFacility> findAllByName(String name);
    public List<MedicalFacility> findAllByAddress(String address);
    List<MedicalFacility> findAllByNameContainingIgnoreCase(String name);
    List<MedicalFacility> findAllByAddressContainingIgnoreCase(String address);
}

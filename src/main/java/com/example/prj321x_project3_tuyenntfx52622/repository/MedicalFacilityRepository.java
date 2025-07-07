package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalFacilityRepository extends JpaRepository<ResetPasswordToken,Long> {
}

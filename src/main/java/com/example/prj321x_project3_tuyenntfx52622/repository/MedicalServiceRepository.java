package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalServiceRepository extends JpaRepository<MedicalService,Long> {
    public boolean existsByServiceName(String serviceName);
}

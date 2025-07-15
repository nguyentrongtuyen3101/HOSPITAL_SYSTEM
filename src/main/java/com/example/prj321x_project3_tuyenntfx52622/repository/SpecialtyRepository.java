package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {
    public boolean existsByName(String name);
    List<Specialty> findTop5ByOrderBySearchnumberDesc();
}

package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.DoctorSpecialty;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalService;
import com.example.prj321x_project3_tuyenntfx52622.entity.Specialty;
import org.springframework.dao.DataAccessException;

public interface admin_service {
    public Specialty createChuyenKhoa(Specialty specialty) throws DataAccessException, IllegalArgumentException;
    public Doctor createdDoctor(Doctor doctor) throws DataAccessException, IllegalArgumentException;
    public DoctorSpecialty createDoctorSpecialty(DoctorSpecialty doctorSpecialty) throws DataAccessException, IllegalArgumentException;
    public String banacc(String role,Long id,String lock_reasion) throws DataAccessException, IllegalArgumentException;
    public MedicalService createDichVuKham(MedicalService medicalService) throws DataAccessException,IllegalArgumentException;
}

package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface admin_service {
    public Specialty createChuyenKhoa(Specialty specialty) throws DataAccessException, IllegalArgumentException;
    public Doctor createdDoctor(Doctor doctor) throws DataAccessException, IllegalArgumentException;
    public DoctorSpecialty createDoctorSpecialty(DoctorSpecialty doctorSpecialty) throws DataAccessException, IllegalArgumentException;
    public String banacc(String role,Long id,String lock_reasion) throws DataAccessException, IllegalArgumentException;
    public MedicalService createDichVuKham(MedicalService medicalService) throws DataAccessException,IllegalArgumentException;
    public MedicalFacility createMedicalFacility(MedicalFacility medicalFacility) throws DataAccessException, IllegalArgumentException;
    public FacilityService createFacilityService(FacilityService facilityService) throws DataAccessException, IllegalArgumentException;
    public List<MedicalService> getlistdichvu(Long id) throws DataAccessException, IllegalArgumentException;
    public FacilityService updateGiaDichVu(Long id,String gia) throws DataAccessException, IllegalArgumentException;

}

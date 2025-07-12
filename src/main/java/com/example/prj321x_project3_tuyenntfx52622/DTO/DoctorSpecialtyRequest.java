package com.example.prj321x_project3_tuyenntfx52622.DTO;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalFacility;
import com.example.prj321x_project3_tuyenntfx52622.entity.Specialty;
import lombok.Data;

import java.util.List;

@Data
public class DoctorSpecialtyRequest {
    private Long doctorId;
    private String fullName;
    private Doctor.Gender gender;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String introduction;
    private String education;
    private String achievements;
    private Long medicalFacilityId;
    private List<Long> specialtyIds;
}

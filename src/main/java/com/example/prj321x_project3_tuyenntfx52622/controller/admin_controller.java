package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.DoctorSpecialtyRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.MedicalServiceRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.SpecialtyRequest;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.DoctorSpecialty;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalService;
import com.example.prj321x_project3_tuyenntfx52622.entity.Specialty;
import com.example.prj321x_project3_tuyenntfx52622.repository.AdminRepository;
import com.example.prj321x_project3_tuyenntfx52622.repository.SpecialtyRepository;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import com.example.prj321x_project3_tuyenntfx52622.service.account_service;
import com.example.prj321x_project3_tuyenntfx52622.service.admin_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class admin_controller {
    @Autowired
    private admin_service admin_service;
    @Autowired
    private account_service account_service;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    public boolean checkAccountAdmin(@AuthenticationPrincipal UserDetails user){
        return adminRepository.existsByEmail(user.getUsername());
    }
    @PostMapping("/createSpecialty")
    public ResponseEntity<?> createSpecialty(@RequestBody SpecialtyRequest specialtyRequest,@AuthenticationPrincipal UserDetails user)
    {
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        Specialty specialty = new Specialty();
        specialty.setName(specialtyRequest.getName());
        specialty.setDescription(specialtyRequest.getDescription());
        admin_service.createChuyenKhoa(specialty);
        Map<String,Object> response = new HashMap<>();
        response.put("name",specialty.getName());
        response.put("description",specialty.getDescription());
        response.put("message","Them chuyen khoa thanh cong ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/createDoctor")
    public ResponseEntity<?>createDoctor(@RequestBody DoctorSpecialtyRequest doctorSpecialtyRequest,@AuthenticationPrincipal UserDetails user)
    {
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        if(doctorSpecialtyRequest.getSpecialtyIds()==null)throw new IllegalArgumentException("Vui long chon it nhat mot chuyen khoa");
        Doctor doctor = new Doctor();
        doctor.setEmail(doctorSpecialtyRequest.getEmail());
        doctor.setPassword(doctorSpecialtyRequest.getPassword());
        doctor.setFullName(doctorSpecialtyRequest.getFullName());
        doctor.setAddress(doctorSpecialtyRequest.getAddress());
        doctor.setPhone(doctorSpecialtyRequest.getPhone());
        doctor.setGender(doctorSpecialtyRequest.getGender());
        doctor.setAchievements(doctorSpecialtyRequest.getAchievements());
        doctor.setEducation( doctorSpecialtyRequest.getEducation());
        doctor.setIntroduction(doctorSpecialtyRequest.getIntroduction());
        Doctor doctorsave=admin_service.createdDoctor(doctor);
        if(doctorsave != null)
        {
            for(Long specialtyId: doctorSpecialtyRequest.getSpecialtyIds()) {
                DoctorSpecialty doctorspecialty = new DoctorSpecialty();
                doctorspecialty.setSpecialty(specialtyRepository.findById(specialtyId).get());
                doctorspecialty.setDoctor(doctorsave);
                admin_service.createDoctorSpecialty(doctorspecialty);
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("message","Tao thanh cong thong tin bac sy : ");
        response.put("fullName",doctorsave.getFullName());
        response.put("email",doctorsave.getEmail());
        response.put("phone",doctorsave.getPhone());
        response.put("gender",doctorsave.getGender());
        response.put("address",doctorsave.getAddress());
        response.put("achievements",doctorsave.getAchievements());
        response.put("education",doctorsave.getEducation());
        response.put("introduction",doctorsave.getIntroduction());
        response.put("specialties", doctorSpecialtyRequest.getSpecialtyIds().stream()
                .map(id -> specialtyRepository.findById(id).map(Specialty::getName).orElse("Unknown"))
                .collect(Collectors.joining(", ")));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/banAcc")
    public ResponseEntity<?>banAcc(@RequestBody Map<String,Object> request,@AuthenticationPrincipal UserDetails user)
    {
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        String idStr = String.valueOf(request.get("id"));
        long id = Long.parseLong(idStr);
        String lock_reasion=(String)request.get("lock_reasion");
        String role=(String)request.get("role");
        if(lock_reasion==null||lock_reasion.equals("")||lock_reasion.isEmpty()) lock_reasion="May da vi pham tieu chuan cong dong";
        String message=admin_service.banacc(role,id,lock_reasion);
        Map<String,Object> response = new HashMap<>();
        response.put("message",message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("createDichvuKham")
    public ResponseEntity<?>CreateDichvuKham(@RequestBody MedicalServiceRequest medicalServiceRequest,@AuthenticationPrincipal UserDetails user)
    {
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        MedicalService medicalService=new MedicalService();
        medicalService.setServiceName(medicalServiceRequest.getServiceName());
        medicalService.setDescription(medicalServiceRequest.getDescription());
        admin_service.createDichVuKham(medicalService);
        Map<String,Object> response = new HashMap<>();
        response.put("serviceName",medicalServiceRequest.getServiceName());
        response.put("description",medicalServiceRequest.getDescription());
        response.put("message","Them dich vu kham thanh cong");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

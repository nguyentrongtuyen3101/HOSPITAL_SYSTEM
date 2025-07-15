package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.DoctorSpecialtyRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.FacilityServiceRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.MedicalServiceRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.SpecialtyRequest;
import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class Admin_controller {
    @Autowired
    private admin_service admin_service;
    @Autowired
    private DoctorRepository doctor_repository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MedicalServiceRepository medicalServiceRepository;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private FacilityServiceRepository facilityServiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
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
        if(doctorSpecialtyRequest.getSpecialtyIds()==null||doctorSpecialtyRequest.getSpecialtyIds().size()<=0)throw new IllegalArgumentException("Vui long chon it nhat mot chuyen khoa");
        if(doctorSpecialtyRequest.getMedicalFacilityId()==null||doctorSpecialtyRequest.getMedicalFacilityId().describeConstable().isEmpty())throw new IllegalArgumentException("Vui long chon 1 co so y te");
        Doctor doctorsave=admin_service.createdDoctor(doctorSpecialtyRequest);
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
        response.put("co so",medicalFacilityRepository.findById(doctorSpecialtyRequest.getMedicalFacilityId()).get().getName());
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
    @PostMapping("/createDichvuKham")
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
    @PostMapping("/createCoSoYTe")
    public ResponseEntity<?>createCoSoYTe(@RequestBody FacilityServiceRequest facilityServiceRequest, @AuthenticationPrincipal UserDetails user)
    {
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        if(facilityServiceRequest.getServiceIds()==null||facilityServiceRequest.getServiceIds().size()<=0)throw new IllegalArgumentException("Vui long chon it nhat mot loai dich vu kham");
        if(facilityServiceRequest.getSpecialtyIds()==null||facilityServiceRequest.getSpecialtyIds().size()<=0)throw new IllegalArgumentException("Vui long chon it nhat 1 chuyen khoa");
        MedicalFacility  medicalFacility=new MedicalFacility();
        medicalFacility.setName(facilityServiceRequest.getName());
        medicalFacility.setAddress(facilityServiceRequest.getAddress());
        medicalFacility.setPhone(facilityServiceRequest.getPhone());
        medicalFacility.setEmail(facilityServiceRequest.getEmail());
        medicalFacility.setDescription(facilityServiceRequest.getDescription());
        MedicalFacility medicalFacilitySave=admin_service.createMedicalFacility(medicalFacility);
        if(medicalFacilitySave!=null)
        {
            for(Long serviceId: facilityServiceRequest.getServiceIds())
            {
                FacilityService facilityService=new FacilityService();
                facilityService.setService(medicalServiceRepository.findById(serviceId).get());
                facilityService.setFacility(medicalFacilitySave);
                facilityService.setCost("0");
                admin_service.createFacilityService(facilityService);
            }
            for (Long SpecialtyId:facilityServiceRequest.getSpecialtyIds())
            {
                FacilitySpecialty facilitySpacialty=new FacilitySpecialty();
                facilitySpacialty.setFacility(medicalFacilitySave);
                facilitySpacialty.setSpecialty(specialtyRepository.findById(SpecialtyId).get());
                admin_service.createFacilitySpacialty(facilitySpacialty);
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("message","Them co so y te thanh cong");
        response.put("name",medicalFacilitySave.getName());
        response.put("description",medicalFacilitySave.getDescription());
        response.put("mail",medicalFacilitySave.getEmail());
        response.put("phone",medicalFacilitySave.getPhone());
        response.put("address",medicalFacilitySave.getAddress());
        response.put("Danh sach dich vu kham ",facilityServiceRequest.getServiceIds().stream()
                .map(id -> medicalServiceRepository.findById(id).map(MedicalService::getServiceName).orElse("Unknown"))
                .collect(Collectors.joining(", ")));
        response.put("chuyen khoa",facilityServiceRequest.getSpecialtyIds().stream()
                .map(id->specialtyRepository.findById(id).map(Specialty::getName).orElse("Unknown"))
                .collect(Collectors.joining(", ")));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/showDichVuCuaCSYT/{facilityId}")
    public ResponseEntity<?>showDichVuCuaCSYT(@PathVariable Long facilityId,@AuthenticationPrincipal UserDetails user){
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        List<MedicalService>medicalServiceList=admin_service.getlistdichvu(facilityId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Danh sách dich vu kham benh co so : "+medicalFacilityRepository.findById(facilityId).get().getName());
        response.put("dich vu : ",medicalServiceList.stream().map(Service->
        {
            Map<String,Object> serviceInfo = new HashMap<>();
            serviceInfo.put("serviceName",Service.getServiceName());
            serviceInfo.put("description",Service.getDescription());
            serviceInfo.put("gia",facilityServiceRepository.findByFacilityAndService(medicalFacilityRepository.findById(facilityId).get(),medicalServiceRepository.findById(Service.getServiceId()).get()).getCost());
            return serviceInfo;
        }));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/updateGiaDV")
    public ResponseEntity<?> updateGiaDV(@AuthenticationPrincipal UserDetails user,@RequestBody FacilityServiceRequest facilityServiceRequest)
    {
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        FacilityService facilityService=admin_service.updateGiaDichVu(facilityServiceRequest.getId(),facilityServiceRequest.getGia());
        Map<String,Object> response = new HashMap<>();
        response.put("message","Update thanh cong gia dich vu "+medicalServiceRepository.findById(facilityService.getService().getServiceId()).get().getServiceName()+" co so "+medicalFacilityRepository.findById(facilityService.getFacility().getFacilityId()).get().getName());
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getLichKham/{idUser}")
    public ResponseEntity<?> getLichKham(@PathVariable Long idUser,@AuthenticationPrincipal UserDetails user){
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        List<Appointment>appointmentList=appointmentRepository.findAllByUser(userRepository.findById(idUser).get());
        List<Map<String,Object>> response=appointmentList.stream().map(appointment -> {
            Map<String,Object> appointmentInfo = new HashMap<>();
            appointmentInfo.put("ma lich kham",appointment.getAppointmentId());
            appointmentInfo.put("ho ten benh nhan",appointment.getUser().getFullName());
            appointmentInfo.put("ho ten bac sy",appointment.getDoctor().getFullName());
            appointmentInfo.put("co so y te",appointment.getFacility().getName());
            appointmentInfo.put("ten dich vu",appointment.getMedicalService().getServiceName());
            appointmentInfo.put("ngay kham",appointment.getAppointmentDate());
            appointmentInfo.put("trieu chung",appointment.getReason());
            appointmentInfo.put("trang thai",appointment.getStatus());
            return appointmentInfo;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getLichKhamBacSy/{idDoctor}")
    public ResponseEntity<?> getLichKhamBácy(@PathVariable Long idDoctor,@AuthenticationPrincipal UserDetails user){
        if(!checkAccountAdmin(user))throw new DataException("Khong tim thay tai khoan "+user.getUsername()+" trong he thong");
        List<Appointment>appointmentList=appointmentRepository.findAllByDoctor(doctor_repository.findById(idDoctor).get());
        List<Map<String,Object>> response=appointmentList.stream().map(appointment -> {
            Map<String,Object> appointmentInfo = new HashMap<>();
            appointmentInfo.put("ma lich kham",appointment.getAppointmentId());
            appointmentInfo.put("ho ten benh nhan",appointment.getUser().getFullName());
            appointmentInfo.put("ho ten bac sy",appointment.getDoctor().getFullName());
            appointmentInfo.put("co so y te",appointment.getFacility().getName());
            appointmentInfo.put("ten dich vu",appointment.getMedicalService().getServiceName());
            appointmentInfo.put("ngay kham",appointment.getAppointmentDate());
            appointmentInfo.put("trieu chung",appointment.getReason());
            appointmentInfo.put("trang thai",appointment.getStatus());
            return appointmentInfo;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

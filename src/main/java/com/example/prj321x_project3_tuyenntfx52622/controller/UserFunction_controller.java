package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.AppointmentRequest;
import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import com.example.prj321x_project3_tuyenntfx52622.service.UserFunction_service;
import com.example.prj321x_project3_tuyenntfx52622.service.account_service;
import com.example.prj321x_project3_tuyenntfx52622.service.admin_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserFunction_controller {
    @Autowired
    private UserFunction_service userFunction_service;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MedicalServiceRepository medicalServiceRepository;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private FacilityServiceRepository facilityServiceRepository;
    public boolean checkAccountUser(@AuthenticationPrincipal UserDetails user){
        return userRepository.existsUsersByEmail(user.getUsername());
    }
    @PostMapping("/datLichKham")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest, @AuthenticationPrincipal UserDetails user){
        if(!checkAccountUser(user))throw new DataException("Khong tim thay tai khoan voi gmail "+user.getUsername());
        Appointment appointment = new Appointment();
        appointment.setUser(userRepository.findUserByEmail(user.getUsername()));
        appointment.setFacility(medicalFacilityRepository.findById(appointmentRequest.getFacilityId()).get());
        appointment.setDoctor(doctorRepository.findById(appointmentRequest.getDoctorId()).get());
        appointment.setMedicalService(medicalServiceRepository.findById(appointmentRequest.getMedicalServiceId()).get());
        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        appointment.setReason(appointmentRequest.getReason());
        Appointment appointmentSave = userFunction_service.createAppointment(appointment);
        Map<String,Object> response = new HashMap<>();
        response.put("ho ten",appointmentSave.getUser().getFullName());
        response.put("doctor",appointmentSave.getDoctor().getFullName());
        response.put("co so y te",appointmentSave.getFacility().getName());
        response.put("dich vu kham",appointmentSave.getMedicalService().getServiceName());
        response.put("gia kham",facilityServiceRepository.findByFacilityAndService(appointmentSave.getFacility(),appointmentSave.getMedicalService()).getCost()+"USD");
        response.put("Ngay hen kham",appointmentSave.getAppointmentDate());
        response.put("Ngay dat lich",appointmentSave.getCreatedAt());
        response.put("reason",appointmentSave.getReason());
        response.put("status",appointmentSave.getStatus());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

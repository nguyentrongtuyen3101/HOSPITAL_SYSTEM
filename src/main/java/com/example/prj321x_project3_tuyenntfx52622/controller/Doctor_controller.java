package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.AppointmentRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.MedicalRecordRequest;
import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalRecord;
import com.example.prj321x_project3_tuyenntfx52622.repository.AppointmentRepository;
import com.example.prj321x_project3_tuyenntfx52622.repository.DoctorRepository;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import com.example.prj321x_project3_tuyenntfx52622.service.Doctor_service;
import com.example.prj321x_project3_tuyenntfx52622.service.UserFunction_service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class Doctor_controller {
    @Autowired
    private Doctor_service doctorService;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserFunction_service userFunction_service;
    @Autowired
    private JavaMailSender mailSender;
    public boolean checkAccountDoctor(@AuthenticationPrincipal UserDetails user){
        return doctorRepository.existsByEmail(user.getUsername());
    }
    @GetMapping("/checkLichKham")
    public ResponseEntity<?> checkLichKham(@AuthenticationPrincipal UserDetails user){
        if(!checkAccountDoctor(user))throw new DataException("Khong tìm thay tai khoan bac sy voi gmail "+user.getUsername());
        Doctor doctor = doctorRepository.findByEmail(user.getUsername());
        List<Appointment> appointmentList=doctorService.getAppointments(doctor);
        List<Map<String,Object>> response= appointmentList.stream().map(appointment -> {
            Map<String,Object> appointmentInfo=new HashMap<>();
            appointmentInfo.put("ho ten",appointment.getUser().getFullName());
            appointmentInfo.put("gioi tinh",appointment.getUser().getGender());
            appointmentInfo.put("dia chi",appointment.getUser().getAddress());
            appointmentInfo.put("dich vu",appointment.getMedicalService().getServiceName());
            appointmentInfo.put("ngay kham",appointment.getAppointmentDate());
            appointmentInfo.put("benh ly",appointment.getReason());
            appointmentInfo.put("trang thai",appointment.getStatus());
            return appointmentInfo;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/huyLichKham/{id}")
    public ResponseEntity<?> huyLichKham(@AuthenticationPrincipal UserDetails user, @PathVariable Long id, @RequestBody AppointmentRequest appointmentRequest){
        if(!checkAccountDoctor(user))throw new DataException("Khong tìm thay tai khoan bac sy voi gmail "+user.getUsername());
        userFunction_service.cancelAppoitment(id,appointmentRequest.getCancelReason(),user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    // chức năng nâng cao
    @PostMapping("/GuiVaCapNhatKQ")
    public ResponseEntity<?> GuiVaCapNhatKQ(@AuthenticationPrincipal UserDetails user,@RequestBody MedicalRecordRequest medicalRecordRequest) {
        if (!checkAccountDoctor(user))throw new DataException("Khong tìm thay tai khoan bac sy voi gmail " + user.getUsername());
        medicalRecordRequest.setDoctorId(doctorRepository.findByEmail(user.getUsername()).getDoctorId());
        MedicalRecord medicalRecord = doctorService.createMedicalRecord(medicalRecordRequest);
        if (medicalRecord != null) {
            doctorService.COMPLETEDAppoitment(medicalRecord.getAppointment().getAppointmentId(), user.getUsername());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("ma ket qua", medicalRecord.getRecordId());
        response.put("ho ten bac sy", medicalRecord.getDoctor().getFullName());
        response.put("ma lich kham", medicalRecord.getAppointment().getAppointmentId());
        response.put("message","Ket qua da dc gui den gmail cua benh nhan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

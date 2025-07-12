package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalService;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserFunction_service_imp implements UserFunction_service{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private MedicalServiceRepository medicalServiceRepository;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private FacilityServiceRepository facilityServiceRepository;
    @Override
    @Transactional
    public Appointment createAppointment(Appointment appointment)
    {
        if(medicalFacilityRepository.findById(appointment.getFacility().getFacilityId()).get()==null)throw new DataException("Khong thay co so y te nay");
        if(doctorRepository.findById(appointment.getDoctor().getDoctorId()).get()==null||doctorRepository.findById(appointment.getDoctor().getDoctorId()).get().getMedicalFacility().getFacilityId()!=appointment.getFacility().getFacilityId())throw new DataException("Bac sy khong ton tai hoac ko nam trong co so y te nay");
        if(facilityServiceRepository.findByFacilityAndService(appointment.getFacility(),appointment.getMedicalService())==null)throw new DataException("Khong co dich vu nay hoac dich vu khong nam trong co so y te nay");
        if(appointment.getAppointmentDate().isBefore(LocalDateTime.now()) || appointment.getAppointmentDate().isEqual(LocalDateTime.now()))throw new IllegalArgumentException("Khong dc dat lich kham muon hon thoi diem hien tai");
        if(appointment.getAppointmentDate()==null)throw new IllegalArgumentException("Khong dc de lich kham trong");
        return appointmentRepository.save(appointment);
    }
}

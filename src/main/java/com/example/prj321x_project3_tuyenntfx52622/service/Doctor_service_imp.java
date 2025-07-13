package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Doctor_service_imp implements Doctor_service {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Override
    @Transactional
    public List<Appointment> getAppointments(Doctor doctor)
    {
        return appointmentRepository.findAllByDoctor(doctor);
    }
}

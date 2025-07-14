package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalRecord;
import com.example.prj321x_project3_tuyenntfx52622.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long> {
    public MedicalRecord findByAppointment(Appointment appointment);
}

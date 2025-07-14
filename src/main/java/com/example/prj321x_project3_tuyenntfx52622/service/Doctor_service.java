package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.DTO.MedicalRecordRequest;
import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalRecord;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;

import java.util.List;

public interface Doctor_service {
    public List<Appointment> getAppointments(Doctor doctor) throws DataException;
    public MedicalRecord createMedicalRecord(MedicalRecordRequest medicalRecordRequest) throws DataException;
    public void COMPLETEDAppoitment(Long id,String mail)throws DataException;
}

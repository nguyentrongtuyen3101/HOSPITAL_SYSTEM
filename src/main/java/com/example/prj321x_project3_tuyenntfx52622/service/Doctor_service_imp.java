package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.DTO.MedicalRecordRequest;
import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalRecord;
import com.example.prj321x_project3_tuyenntfx52622.repository.AppointmentRepository;
import com.example.prj321x_project3_tuyenntfx52622.repository.DoctorRepository;
import com.example.prj321x_project3_tuyenntfx52622.repository.MedicalRecordRepository;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class Doctor_service_imp implements Doctor_service {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Override
    @Transactional
    public List<Appointment> getAppointments(Doctor doctor)
    {
        return appointmentRepository.findAllByDoctor(doctor);
    }
    @Override
    @Transactional
    public MedicalRecord createMedicalRecord(MedicalRecordRequest medicalRecordRequest)
    {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setDoctor(doctorRepository.findById(medicalRecordRequest.getDoctorId()).get());
        medicalRecord.setAppointment(appointmentRepository.findById(medicalRecordRequest.getAppointmentId()).get());
        medicalRecord.setDiagnosis(medicalRecordRequest.getDiagnosis());
        medicalRecord.setTreatment(medicalRecordRequest.getTreatment());
        medicalRecord.setNotes(medicalRecordRequest.getNotes());
        if(!doctorRepository.findById(medicalRecordRequest.getDoctorId()).get().getEmail().equals(doctorRepository.findById(medicalRecordRequest.getDoctorId()).get().getEmail()))throw new DataException("khong dc cap nhat ket qua kham cua bac sy khac");
        if(medicalRecordRepository.findByAppointment(medicalRecord.getAppointment())!=null)throw new DataException("Lich hen kham nay da duoc xu ly");
        if(medicalRecord.getAppointment().getStatus().toString().equals("CANCELLED"))throw new DataException("Lich kham nay da bi huy");
        return medicalRecordRepository.save(medicalRecord);
    }
    @Override
    @Transactional
    public void COMPLETEDAppoitment(Long id,String mail)
    {
        Appointment appointment = appointmentRepository.findById(id).get();
        if(!appointment.getDoctor().getEmail().equals(mail))throw new DataException("Khong dc cap nhat lich kham cua bac sy khac");
        if(appointment!=null)
        {
            appointment.setStatus(Appointment.Status.COMPLETED);
            appointment.setCancelReason("well done");
            appointmentRepository.save(appointment);
        }
    }

}

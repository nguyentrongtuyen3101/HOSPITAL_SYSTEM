package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private UserRepository userRepository;
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
    @Override
    @Transactional
    public List<Appointment> getAppointmentsbyUser(User user)
    {
        return appointmentRepository.findAllByUser(user);
    }
    @Override
    @Transactional
    public void cancelAppoitment(Long id,String cancelReason,String mail)
    {
        Appointment appointment = appointmentRepository.findById(id).get();
        if(userRepository.findUserByEmail(mail)!=null){
            if(!appointment.getUser().getEmail().equals(mail))throw new DataException("Khong dc cap nhat lich kham cua nguoi khac");
        } else if (doctorRepository.findByEmail(mail)!=null) {
            if(!appointment.getDoctor().getEmail().equals(mail))throw new DataException("Khong dc cap nhat lich kham cua bac sy khac");
        }
        if(appointment!=null&&("PENDING".equals(appointment.getStatus().toString())||"CONFIRMED".equals(appointment.getStatus().toString())))
        {
            appointment.setStatus(Appointment.Status.CANCELLED);
            if(cancelReason==null||cancelReason.length()<=0)appointment.setCancelReason("Khong co ly do");
            else appointment.setCancelReason(cancelReason);
            appointmentRepository.save(appointment);
        }
    }
    @Override
    @Transactional
    public List<MedicalFacility> getMedicalFacilities()
    {
        return medicalFacilityRepository.findAll();
    }
}

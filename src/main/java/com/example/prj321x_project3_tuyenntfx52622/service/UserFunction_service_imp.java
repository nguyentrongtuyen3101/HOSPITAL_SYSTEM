package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserFunction_service_imp implements UserFunction_service{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private FacilitySpecialtyRepository facilitySpecialtyRepository;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private FacilityServiceRepository facilityServiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpecialtyRepository  specialtyRepository;
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
    @Override
    @Transactional
    public  List<MedicalFacility> getMedicalFacilitys(Integer searchType,String Keyword,float gia,Long specialtyId)
    {
        List<MedicalFacility> medicalFacilityList=null;
        if(searchType==1)
        {
            medicalFacilityList= medicalFacilityRepository.findAllByNameContainingIgnoreCase(Keyword.trim());
        }
        if(searchType==2)
        {
            medicalFacilityList= medicalFacilityRepository.findAllByAddressContainingIgnoreCase(Keyword.trim());
        }

        if(gia==0&&specialtyId==0)return medicalFacilityList;
        else if(gia!=0&&specialtyId==0)
        {
            List<MedicalFacility> medicalFacilityList1=new ArrayList<>(medicalFacilityList);
            for (MedicalFacility medicalFacility : medicalFacilityList)
            {
                List<FacilityService>facilityServices=facilityServiceRepository.findFacilityServicesByFacility(medicalFacility);
                float total=0;
                for(FacilityService facilityService:facilityServices)
                {
                    total+=Float.parseFloat(facilityService.getCost());
                }
                float giaTB=total/facilityServices.size();
                if((giaTB>gia&&giaTB-gia>=50)||(giaTB<gia&&gia-giaTB>=50))medicalFacilityList1.remove(medicalFacility);
            }
            if(medicalFacilityList1.size()==0)throw new DataException("Khong co du lieu");
            return medicalFacilityList1;
        } else if (gia==0&&specialtyId!=0)
        {
            List<MedicalFacility> medicalFacilityList1=new ArrayList<>(medicalFacilityList);
            for (MedicalFacility medicalFacility : medicalFacilityList)
            {
               if(!facilitySpecialtyRepository.existsByFacilityAndSpecialty(medicalFacility,specialtyRepository.findById(specialtyId).get()))medicalFacilityList1.remove(medicalFacility);
            }
            if(medicalFacilityList1.size()==0)throw new DataException("Khong co du lieu");
            Specialty specialty=specialtyRepository.findById(specialtyId).get();
            specialty.setSearchnumber(specialty.getSearchnumber()+1);
            specialtyRepository.save(specialty);
            return medicalFacilityList1;
        }
        else {
            List<MedicalFacility> medicalFacilityList1=new ArrayList<>(medicalFacilityList);
            for (MedicalFacility medicalFacility : medicalFacilityList)
            {
                List<FacilityService>facilityServices=facilityServiceRepository.findFacilityServicesByFacility(medicalFacility);
                float total=0;
                for(FacilityService facilityService:facilityServices)
                {
                    total+=Float.parseFloat(facilityService.getCost());
                }
                float giaTB=total/facilityServices.size();
                if((giaTB>gia&&giaTB-gia>=50)||(giaTB<gia&&gia-giaTB>=50))medicalFacilityList1.remove(medicalFacility);
            }
            List<MedicalFacility> medicalFacilityList2=new ArrayList<>(medicalFacilityList1);
            for (MedicalFacility medicalFacility : medicalFacilityList1)
            {
                if(!facilitySpecialtyRepository.existsByFacilityAndSpecialty(medicalFacility,specialtyRepository.findById(specialtyId).get()))medicalFacilityList2.remove(medicalFacility);
            }
            if(medicalFacilityList2.size()==0)throw new DataException("Khong co du lieu");
            Specialty specialty=specialtyRepository.findById(specialtyId).get();
            specialty.setSearchnumber(specialty.getSearchnumber()+1);
            specialtyRepository.save(specialty);
            return medicalFacilityList2;
        }
    }
}

package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class admin_service_imp implements admin_service{
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DoctorSpecialtyRepository doctorSpecialtyRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MedicalServiceRepository medicalServiceRepository;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private FacilityServiceRepository facilityServiceRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)(\\d{9})$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    @Override
    @Transactional
    public Specialty createChuyenKhoa(Specialty specialty)
    {
        if(specialty.getName()==null||specialty.getName().isEmpty())throw new IllegalArgumentException("Khong dc de chong ten chuyen khoa !!!");
        if(specialtyRepository.existsByName(specialty.getName().trim()))throw new DataException("Chuyen khoa "+specialty.getName()+" da co trong he thong");
        return  specialtyRepository.save(specialty);
    }
    @Override
    @Transactional
    public Doctor createdDoctor(Doctor doctor)
    {
        if(doctor.getFullName()==null||doctor.getFullName().trim().isEmpty()){
            throw new IllegalArgumentException("Ten ko dc de chong");
        }
        if(doctor.getEmail()==null||!EMAIL_PATTERN.matcher(doctor.getEmail()).matches()){
            throw new IllegalArgumentException("gmail ko dc de chong hoac khong hop le");
        }
        if(doctor.getPhone()==null||!PHONE_PATTERN.matcher(doctor.getPhone()).matches()){
            throw new IllegalArgumentException("So dien thoai ko dc de chong hop le");
        }
        if(doctor.getPassword()==null||doctor.getPassword().trim().isEmpty()||!PASSWORD_PATTERN.matcher(doctor.getPassword()).matches()){
            throw new IllegalArgumentException("Mat khau ko dc de chong va phai co chu hoa, chu thuong, so, ky tu dac biet và toi thieu 8 ky tu");
        }
        if(doctor.getGender()==null)
        {
            throw new IllegalArgumentException("Vui long chon gioi tinh");
        }
        if(doctor.getAddress()==null||doctor.getAddress().trim().isEmpty())
        {
            throw new IllegalArgumentException("Dia chi ko dc de chong");
        }
        if(userRepository.existsUsersByEmail(doctor.getEmail())||adminRepository.existsByEmail(doctor.getEmail())||doctorRepository.existsByEmail(doctor.getEmail()))
        {
            throw new DataException("Gmail "+doctor.getEmail().toString()+" da dc dang ky");
        }
        String encodedPassword = passwordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        return doctorRepository.save(doctor);
    }
    @Override
    @Transactional
    public DoctorSpecialty createDoctorSpecialty(DoctorSpecialty doctorSpecialty)
    {
        if(doctorSpecialty.getSpecialty()==null)throw new IllegalArgumentException("Chọn một chuyên khoa bác sỹ phụ trách");
        return  doctorSpecialtyRepository.save(doctorSpecialty);
    }
    @Override
    @Transactional
    public String banacc(String role,Long id,String lock_reasion)
    {
        String message="";
        if(role.equals("docter"))
        {
            Doctor doctor=doctorRepository.findById(id).get();
            if(doctor.isLocked())
            {
                doctor.setLocked(false);
                message="Mo khoa thanh cong tai khoan "+doctor.getFullName();
            }
            else
            {
                doctor.setLocked(true);
                message="Khoa thanh cong tai khoan "+doctor.getFullName();
            }
            doctor.setLockReason(lock_reasion);
            doctorRepository.save(doctor);
        } else if (role.equals("user")) {
            User user=userRepository.findById(id).get();
            if(user.isLocked())
            {
                user.setLocked(false);
                message="Mo khoa thanh cong tai khoan "+user.getFullName();
            }
            else
            {
                user.setLocked(true);
                message="Khoa thanh cong tai khoan "+user.getFullName();
            }
            user.setLockReason(lock_reasion);
            userRepository.save(user);
        }
        return message;
    }
    @Override
    @Transactional
    public MedicalService createDichVuKham(MedicalService medicalService)
    {
        if(medicalService.getServiceName()==null||medicalService.getServiceName().trim().isEmpty())throw new IllegalArgumentException("Khong dc de chong ten dich vu");
        if(medicalServiceRepository.existsByServiceName(medicalService.getServiceName().trim()))throw new DataException("Dich vu "+medicalService.getServiceName()+" da co trong he thong");
        return medicalServiceRepository.save(medicalService);
    }
    @Override
    @Transactional
    public MedicalFacility createMedicalFacility(MedicalFacility medicalFacility)
    {
        if(medicalFacility.getName()==null||medicalFacility.getName().trim().isEmpty())throw new IllegalArgumentException("Khong dc de chong ten so so");
        if(medicalFacility.getAddress()==null||medicalFacility.getAddress().trim().isEmpty())throw new IllegalArgumentException("Khong dc de chong dia chi");
        if(medicalFacility.getEmail()==null||medicalFacility.getEmail().trim().isEmpty())throw new IllegalArgumentException("Khong dc de chong gmail");
        if(medicalFacility.getPhone()==null||medicalFacility.getPhone().trim().isEmpty())throw new IllegalArgumentException("Khong dc de chong phone");
        if(medicalFacilityRepository.existsByName(medicalFacility.getName().trim()))throw new DataException("Da ton tai co so y te nay trong he thong");
        if(medicalFacilityRepository.existsByAddress(medicalFacility.getAddress()))throw new DataException("Da ton tai dia chi nay");
        return medicalFacilityRepository.save(medicalFacility);
    }
    @Override
    @Transactional
    public FacilityService createFacilityService(FacilityService facilityService)
    {
        return facilityServiceRepository.save(facilityService);
    }
    @Override
    @Transactional
    public List<MedicalService> getlistdichvu(Long id)
    {
        List<FacilityService> facilityServices=facilityServiceRepository.findFacilityServicesByFacility(medicalFacilityRepository.findById(id).get());
        List<MedicalService> medicalServices=new ArrayList<MedicalService>();
        for(FacilityService facilityService:facilityServices)
        {
            medicalServices.add(medicalServiceRepository.findById(facilityService.getService().getServiceId()).get());
        }
        return medicalServices;
    }
    @Override
    @Transactional
    public FacilityService updateGiaDichVu(Long id,String gia)
    {
        if((Float.parseFloat(gia)<0))throw  new IllegalArgumentException("Khong dc nhap gia dich vu be hon 0");
        FacilityService facilityService=facilityServiceRepository.findById(id).get();
        facilityService.setCost(gia);
        return facilityServiceRepository.save(facilityService);
    }
}

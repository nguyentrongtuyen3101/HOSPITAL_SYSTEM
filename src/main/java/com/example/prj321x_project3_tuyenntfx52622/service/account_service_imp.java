package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Admin;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import com.example.prj321x_project3_tuyenntfx52622.repository.AdminRepository;
import com.example.prj321x_project3_tuyenntfx52622.repository.DoctorRepository;
import com.example.prj321x_project3_tuyenntfx52622.repository.UserRepository;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class account_service_imp implements account_service {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)(\\d{9})$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    @Override
    @Transactional
    public User dangky(User user) {
        if(user.getFullName()==null||user.getFullName().trim().isEmpty()){
            throw new IllegalArgumentException("Ten ko dc de chong");
        }
        if(user.getEmail()==null||!EMAIL_PATTERN.matcher(user.getEmail()).matches()){
            throw new IllegalArgumentException("gmail ko dc de chong hoac khong hop le");
        }
        if(user.getPhone()==null||!PHONE_PATTERN.matcher(user.getPhone()).matches()){
            throw new IllegalArgumentException("So dien thoai ko dc de chong hop le");
        }
        if(user.getPassword()==null||user.getPassword().trim().isEmpty()||!PASSWORD_PATTERN.matcher(user.getPassword()).matches()){
            throw new IllegalArgumentException("Mat khau ko dc de chong va phai co chu hoa, chu thuong, so, ky tu dac biet và toi thieu 8 ky tu");
        }
        if(user.getGender()==null)
        {
            throw new IllegalArgumentException("Vui long chon gioi tinh");
        }
        if(user.getAddress()==null||user.getAddress().trim().isEmpty())
        {
            throw new IllegalArgumentException("Dia chi ko dc de chong");
        }
        if(userRepository.existsUsersByEmail(user.getEmail())||adminRepository.existsByEmail(user.getEmail())||doctorRepository.existsByEmail(user.getEmail()))
        {
            throw new DataException("Gmail "+user.getEmail().toString()+" da dc dang ky");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
    @Override
    @Transactional
    public User dangnhapUser(String email, String password)
    {
        User user = userRepository.findUserByEmail(email);
        if(user==null)throw new DataException("khong tim thay tai khoan voi email : "+email );
        if (!passwordEncoder.matches(password, user.getPassword())) throw new DataException("Mat khau khong chinh xac");
        if(user.isLocked())throw new DataException("Tai khoan nay da bi khoa vi "+user.getLockReason());
        return user;
    }
    @Override
    @Transactional
    public Admin dangnhapAdmin(String email, String password)
    {
        Admin admin = adminRepository.findByEmail(email);
        if(admin==null)throw new DataException("khong tim thay tai khoan voi email : "+email );
        if (!passwordEncoder.matches(password, admin.getPassword())) throw new DataException("Mat khau khong chinh xac");
        if(admin.isLocked())throw new DataException("Tai khoan nay da bi khoa !!!");
        return admin;
    }
    @Override
    @Transactional
    public Doctor dangnhapDoctor(String email, String password)
    {
        Doctor doctor = doctorRepository.findByEmail(email);
        if(doctor==null)throw new DataException("khong tim thay tai khoan voi email : "+email );
        if (!passwordEncoder.matches(password, doctor.getPassword())) throw new DataException("Mat khau khong chinh xac");
        if(doctor.isLocked())throw new DataException("Tai khoan nay da bi khoa vi "+doctor.getLockReason());
        return doctor;
    }
    @Override
    @Transactional
    public String checkmail(String mail)
    {
        if(userRepository.findUserByEmail(mail)==null&&adminRepository.findByEmail(mail)==null&&doctorRepository.findByEmail(mail)==null) throw new DataException("Khong tim thay tai khoan voi email : "+mail);
        else if (userRepository.findUserByEmail(mail)!=null) {
            return "user";
        }
        else if (adminRepository.findByEmail(mail)!=null) {
            return "admin";
        }
        else if (doctorRepository.findByEmail(mail)!=null) {
            return "doctor";
        }
        return mail;
    }
    @Override
    @Transactional
    public void doimk(String role,String mail,String password)
    {
        if(password==null||password.trim().isEmpty()||!PASSWORD_PATTERN.matcher(password).matches()){
            throw new IllegalArgumentException("Mat khau ko dc de chong va phai co chu hoa, chu thuong, so, ky tu dac biet và toi thieu 8 ky tu");
        }
        String encodedPassword = passwordEncoder.encode(password);
        if(role.equals("user"))
        {
            User user=userRepository.findUserByEmail(mail);
            user.setPassword(encodedPassword);
            userRepository.save(user);
        } else if (role.equals("admin")) {
            Admin admin=adminRepository.findByEmail(mail);
            admin.setPassword(encodedPassword);
            adminRepository.save(admin);
        } else if (role.equals("doctor")) {
            Doctor doctor=doctorRepository.findByEmail(mail);
            doctor.setPassword(encodedPassword);
            doctorRepository.save(doctor);
        }
    }
}

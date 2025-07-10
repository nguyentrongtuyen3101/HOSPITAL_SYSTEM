package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.UserRequest;
import com.example.prj321x_project3_tuyenntfx52622.config.JwtUtil;
import com.example.prj321x_project3_tuyenntfx52622.entity.Admin;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import com.example.prj321x_project3_tuyenntfx52622.service.account_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/account")
public class account_controller {
    @Autowired
    private account_service account_service;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JavaMailSender mailSender;
    private Map<String, String> otpStore = new HashMap<>();
    private Random random = new Random();
    @PostMapping("/createUser")
    public ResponseEntity<User> dangkytaikhoan(@RequestBody UserRequest userRequest)
    {
        if(!userRequest.getConfirmPassword().equals(userRequest.getPassword())) throw new IllegalArgumentException("Mat khau nhap lại khong chinh xac");
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setFullName(userRequest.getFullName());
        user.setAddress(userRequest.getAddress());
        user.setPhone(userRequest.getPhone());
        user.setGender(userRequest.getGender());
        return new ResponseEntity<>(account_service.dangky(user), HttpStatus.CREATED);
    }
    @PostMapping("/dangnhap")
    public ResponseEntity<?> dangnhap(@RequestBody Map<String, Object> maplogin)
    {
        String gmail = (String) maplogin.get("email");
        String password = (String) maplogin.get("password");
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        String role= account_service.checkmail(gmail);
        if(role.equals("user")){
            User user = account_service.dangnhapUser(gmail,password);
            claims.put("email", user.getEmail());
            claims.put("fullName", user.getFullName());
            claims.put("id", user.getUserId());
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            response.put("token", token);
            response.put("email", user.getEmail());
            response.put("fullName", user.getFullName());
            response.put("role", user.getRole().name());
            response.put("message","Đăng nhập thành công vi tri benh nhan");
        }
        else if (role.equals("admin")) {
            Admin admin = account_service.dangnhapAdmin(gmail,password);
            claims.put("email", admin.getEmail());
            claims.put("fullName", admin.getFullName());
            claims.put("id", admin.getAdminId());
            UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            response.put("token", token);
            response.put("email", admin.getEmail());
            response.put("fullName", admin.getFullName());
            response.put("role", admin.getRole().name());
            response.put("message","Đăng nhập thành công vi tri Admin");
        }
        else if (role.equals("doctor")) {
            Doctor doctor = account_service.dangnhapDoctor(gmail,password);
            claims.put("email", doctor.getEmail());
            claims.put("fullName", doctor.getFullName());
            claims.put("id", doctor.getDoctorId());
            UserDetails userDetails = userDetailsService.loadUserByUsername(doctor.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            response.put("token", token);
            response.put("email", doctor.getEmail());
            response.put("fullName", doctor.getFullName());
            response.put("role", doctor.getRole().name());
            response.put("message","Đăng nhập thành công vi tri Bac sy");
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody UserRequest userRequest)
    {
        String gmail = account_service.checkmail(userRequest.getEmail());
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(userRequest.getEmail(), otp);
        System.out.println("Saved OTP for " + userRequest.getEmail() + ": " + otp);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userRequest.getEmail());
        message.setSubject("Mã OTP đặt lại mật khẩu");
        message.setText("Mã OTP của bạn là: " + otp + "\nMã sẽ hết hạn sau 10 phút.");
        mailSender.send(message);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Vui lòng kiểm tra email để đặt lại mật khẩu");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetpassword(@RequestBody Map<String, Object> request)
    {
        String gmail = (String) request.get("email");
        String otp = (String) request.get("otp");
        String password = (String) request.get("password");
        String confirmPassword = (String) request.get("confirmPassword");
        String storedOtp = otpStore.get(gmail);
        String role= account_service.checkmail(gmail);
        System.out.println("Email: " + gmail + ", Input OTP: " + otp + ", Stored OTP: " + storedOtp);
        if (storedOtp == null || !storedOtp.equals(otp)) throw new IllegalArgumentException("Mã OTP không hợp lệ hoặc đã hết hạn");
        if (!password.equals(confirmPassword)) throw new IllegalArgumentException("Mat khau nhap lai khong khop");
        account_service.doimk(role,gmail,password);
        Map<String, Object> response = new HashMap<>();
        response.put("message","Reset thanh cong mat khau tai khoan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

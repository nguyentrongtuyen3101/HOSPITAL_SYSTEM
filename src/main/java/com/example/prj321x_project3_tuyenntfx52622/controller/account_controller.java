package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.UserRequest;
import com.example.prj321x_project3_tuyenntfx52622.config.JwtUtil;
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
    public ResponseEntity<?> dangnhap(@RequestBody UserRequest userRequest)
    {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        User user2 = account_service.dangnhap(user.getEmail(), user.getPassword());
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("fullName", user.getFullName());
        claims.put("id", user.getUserId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user2.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user2.getEmail());
        response.put("fullName", user2.getFullName());
        response.put("role", user2.getRole().name());
        response.put("message","Đăng nhập thành công");
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody UserRequest userRequest)
    {
        String gmail = account_service.checkmail(userRequest.getEmail());
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStore.put(gmail, otp);
        System.out.println("Saved OTP for " + gmail + ": " + otp);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(gmail);
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
        System.out.println("Email: " + gmail + ", Input OTP: " + otp + ", Stored OTP: " + storedOtp);
        if (storedOtp == null || !storedOtp.equals(otp)) throw new IllegalArgumentException("Mã OTP không hợp lệ hoặc đã hết hạn");
        if (!password.equals(confirmPassword)) throw new IllegalArgumentException("Mat khau nhap lai khong khop");
        User user = new User();
        user.setEmail(gmail);
        user.setPassword(password);
        User userRespone= account_service.doimk(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message","Reset thanh cong mat khau tai khoan [ "+userRespone.getFullName()+", "+userRespone.getEmail()+"]");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

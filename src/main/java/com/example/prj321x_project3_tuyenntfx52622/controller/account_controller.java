package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.UserRequest;
import com.example.prj321x_project3_tuyenntfx52622.config.JwtUtil;
import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import com.example.prj321x_project3_tuyenntfx52622.service.account_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class account_controller {
    @Autowired
    private account_service account_service;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
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
}

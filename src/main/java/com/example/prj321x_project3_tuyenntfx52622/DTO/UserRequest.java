package com.example.prj321x_project3_tuyenntfx52622.DTO;

import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserRequest {

    private String fullName;
    private User.Gender gender;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String confirmPassword;
}

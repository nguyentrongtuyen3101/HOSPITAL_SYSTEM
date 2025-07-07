package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import org.springframework.dao.DataAccessException;

public interface account_service {
    public User dangky(User user) throws DataAccessException,IllegalArgumentException;
    public User dangnhap(String email, String password) throws DataAccessException;
}

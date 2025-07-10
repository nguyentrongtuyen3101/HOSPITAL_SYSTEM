package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Admin;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import org.springframework.dao.DataAccessException;

public interface account_service {
    public User dangky(User user) throws DataAccessException,IllegalArgumentException;
    public User dangnhapUser(String email, String password) throws DataAccessException;
    public Admin dangnhapAdmin(String email, String password) throws DataAccessException;
    public Doctor dangnhapDoctor(String email, String password) throws DataAccessException;
    public String checkmail(String mail) throws DataAccessException,IllegalArgumentException;
    public void doimk(String role,String mail,String password) throws DataAccessException,IllegalArgumentException;
}

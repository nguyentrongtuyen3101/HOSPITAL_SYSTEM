package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsUsersByEmail(String email);
    public User findUserByEmail(String email) ;
}
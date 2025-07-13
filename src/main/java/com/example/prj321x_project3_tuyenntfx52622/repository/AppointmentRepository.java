package com.example.prj321x_project3_tuyenntfx52622.repository;

import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.Doctor;
import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    public List<Appointment> findAllByUser(User user);
    public List<Appointment> findAllByDoctor(Doctor doctor);
}

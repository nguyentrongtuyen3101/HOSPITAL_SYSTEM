package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;

public interface UserFunction_service {
    public Appointment createAppointment(Appointment appointment)throws DataException,IllegalArgumentException;
}

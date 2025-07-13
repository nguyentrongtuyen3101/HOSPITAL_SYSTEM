package com.example.prj321x_project3_tuyenntfx52622.service;

import com.example.prj321x_project3_tuyenntfx52622.entity.Appointment;
import com.example.prj321x_project3_tuyenntfx52622.entity.MedicalFacility;
import com.example.prj321x_project3_tuyenntfx52622.entity.Specialty;
import com.example.prj321x_project3_tuyenntfx52622.entity.User;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;

import java.util.List;

public interface UserFunction_service {
    public Appointment createAppointment(Appointment appointment)throws DataException,IllegalArgumentException;
    public List<Appointment> getAppointmentsbyUser(User user) throws DataException;
    public void cancelAppoitment(Long id,String cancelReason,String mail)throws DataException;
    public List<MedicalFacility> getMedicalFacilities();
}

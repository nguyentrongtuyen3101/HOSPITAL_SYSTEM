package com.example.prj321x_project3_tuyenntfx52622.DTO;
import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class AppointmentRequest {
    private Long userId;
    private Long doctorId;
    private Long facilityId;
    private Long medicalServiceId;
    private LocalDateTime appointmentDate;
    private String reason;
}

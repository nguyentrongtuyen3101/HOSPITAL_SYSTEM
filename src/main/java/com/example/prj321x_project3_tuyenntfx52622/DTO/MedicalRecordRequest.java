package com.example.prj321x_project3_tuyenntfx52622.DTO;
import lombok.Data;
@Data
public class MedicalRecordRequest {
    private Long doctorId;
    private Long appointmentId;
    private String diagnosis;
    private String treatment;
    private String notes;
}

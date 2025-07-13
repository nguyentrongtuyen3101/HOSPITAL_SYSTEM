package com.example.prj321x_project3_tuyenntfx52622.DTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
public class MedicalFacilityResponse {
    private Long facilityId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private List<String> facilitySpecialtyName;
    private List<FacilityServiceResponse> facilityServiceResponseList;
}

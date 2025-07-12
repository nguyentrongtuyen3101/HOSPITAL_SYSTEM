package com.example.prj321x_project3_tuyenntfx52622.DTO;
import lombok.Data;
import java.util.List;
@Data
public class FacilityServiceRequest {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String gia;
    private List<Long> serviceIds;
}

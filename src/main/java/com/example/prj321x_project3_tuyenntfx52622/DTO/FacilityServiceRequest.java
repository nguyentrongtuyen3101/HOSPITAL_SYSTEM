package com.example.prj321x_project3_tuyenntfx52622.DTO;
import lombok.Data;
import java.util.List;
@Data
public class FacilityServiceRequest {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String gia;
    private Integer searchType;
    private Long specialtyId;
    private List<Long> serviceIds;
    private List<Long> specialtyIds;
}

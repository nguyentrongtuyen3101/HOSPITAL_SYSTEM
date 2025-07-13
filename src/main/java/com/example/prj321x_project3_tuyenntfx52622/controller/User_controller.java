package com.example.prj321x_project3_tuyenntfx52622.controller;

import com.example.prj321x_project3_tuyenntfx52622.DTO.AppointmentRequest;
import com.example.prj321x_project3_tuyenntfx52622.DTO.FacilityServiceResponse;
import com.example.prj321x_project3_tuyenntfx52622.DTO.MedicalFacilityResponse;
import com.example.prj321x_project3_tuyenntfx52622.entity.*;
import com.example.prj321x_project3_tuyenntfx52622.repository.*;
import com.example.prj321x_project3_tuyenntfx52622.rest.DataException;
import com.example.prj321x_project3_tuyenntfx52622.service.UserFunction_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class User_controller {
    @Autowired
    private UserFunction_service userFunction_service;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MedicalServiceRepository medicalServiceRepository;
    @Autowired
    private MedicalFacilityRepository medicalFacilityRepository;
    @Autowired
    private FacilityServiceRepository facilityServiceRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private FacilitySpecialtyRepository facilitySpecialtyRepository;
    public boolean checkAccountUser(@AuthenticationPrincipal UserDetails user){
        return userRepository.existsUsersByEmail(user.getUsername());
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/datLichKham")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest appointmentRequest, @AuthenticationPrincipal UserDetails user){
        if(!checkAccountUser(user))throw new DataException("Khong tim thay tai khoan voi gmail "+user.getUsername());
        Appointment appointment = new Appointment();
        appointment.setUser(userRepository.findUserByEmail(user.getUsername()));
        appointment.setFacility(medicalFacilityRepository.findById(appointmentRequest.getFacilityId()).get());
        appointment.setDoctor(doctorRepository.findById(appointmentRequest.getDoctorId()).get());
        appointment.setMedicalService(medicalServiceRepository.findById(appointmentRequest.getMedicalServiceId()).get());
        appointment.setAppointmentDate(appointmentRequest.getAppointmentDate());
        appointment.setReason(appointmentRequest.getReason());
        Appointment appointmentSave = userFunction_service.createAppointment(appointment);
        Map<String,Object> response = new HashMap<>();
        response.put("ho ten",appointmentSave.getUser().getFullName());
        response.put("doctor",appointmentSave.getDoctor().getFullName());
        response.put("co so y te",appointmentSave.getFacility().getName());
        response.put("dich vu kham",appointmentSave.getMedicalService().getServiceName());
        response.put("gia kham",facilityServiceRepository.findByFacilityAndService(appointmentSave.getFacility(),appointmentSave.getMedicalService()).getCost()+"USD");
        response.put("Ngay hen kham",appointmentSave.getAppointmentDate());
        response.put("Ngay dat lich",appointmentSave.getCreatedAt());
        response.put("reason",appointmentSave.getReason());
        response.put("status",appointmentSave.getStatus());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/showProfile")
    public ResponseEntity<?> showProfile(@AuthenticationPrincipal UserDetails user){
        if(!checkAccountUser(user))throw new DataException("Khong tim thay tai khoan voi gmail "+user.getUsername());
        User userprofile = userRepository.findUserByEmail(user.getUsername());
        List<Appointment> appointments = userFunction_service.getAppointmentsbyUser(userprofile);
        Map<String,Object> response = new HashMap<>();
        response.put("ho ten",userprofile.getFullName());
        response.put("Gioi tinh",userprofile.getGender());
        response.put("gmail",userprofile.getEmail());
        response.put("phone",userprofile.getPhone());
        response.put("address",userprofile.getAddress());
        response.put("lich kham",appointments.stream().map(appointment -> {
            Map<String,Object> appointmentInfo = new HashMap<>();
            appointmentInfo.put("co so y te",appointment.getFacility().getName());
            appointmentInfo.put("doctor",appointment.getDoctor().getFullName());
            appointmentInfo.put("medicalService",appointment.getMedicalService().getServiceName());
            appointmentInfo.put("gia kham",appointment.getAppointmentDate()+"USD");
            appointmentInfo.put("Ngay hen kham",appointment.getAppointmentDate());
            appointmentInfo.put("Ngay dat lich",appointment.getAppointmentDate());
            appointmentInfo.put("reason",appointment.getReason());
            appointmentInfo.put("cancelReson",appointment.getCancelReason());
            appointmentInfo.put("status",appointment.getStatus());
            return appointmentInfo;
        }));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/huyLichKham/{id}")
    public ResponseEntity<?> huyLichKham(@AuthenticationPrincipal UserDetails user,@PathVariable Long id,@RequestBody AppointmentRequest appointmentRequest){
        if(!checkAccountUser(user))throw new DataException("Khong tim thay tai khoan voi gmail "+user.getUsername());
        userFunction_service.cancelAppoitment(id,appointmentRequest.getCancelReason(),user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //ở đây có 2 api đều trả về danh sách cơ sở y tế và kết quả đều giống nhau, em chỉ viết để xem dùng map với dùng DTO xem cách nào hiệu quả hơn thôi anh/chị chạy API nào cũng dc ạ
    @GetMapping("/showCoSoYTe")
    public ResponseEntity<?> showCoSoYTe(){
        List<MedicalFacility>medicalFacilityList=userFunction_service.getMedicalFacilities();
        List<Map<String,Object>> response = new ArrayList<>();
        Map<String,Object> cosoyte = new LinkedHashMap<>();
        cosoyte.put("Co So Y Te",medicalFacilityList.stream()
                .map(medicalFacility -> {
                    Map<String,Object> medicalFacilityInfo = new HashMap<>();
                    medicalFacilityInfo.put("name",medicalFacility.getName());
                    medicalFacilityInfo.put("address",medicalFacility.getAddress());
                    medicalFacilityInfo.put("gmail",medicalFacility.getEmail());
                    medicalFacilityInfo.put("phone",medicalFacility.getPhone());
                    medicalFacilityInfo.put("description",medicalFacility.getDescription());
                    List<FacilitySpecialty>facilitySpecialtyList=facilitySpecialtyRepository.findAllByFacility(medicalFacility);
                    medicalFacilityInfo.put("chuyen khoa",facilitySpecialtyList.stream().map(facilitySpecialty ->facilitySpecialty.getSpecialty().getName()).collect(Collectors.joining(", ")));
                    List<FacilityService>facilityServiceList=facilityServiceRepository.findFacilityServicesByFacility(medicalFacility);
                    medicalFacilityInfo.put("service",facilityServiceList.stream().map(facilityService -> {
                        Map<String,Object> facilityServiceInfo = new HashMap<>();
                        facilityServiceInfo.put("name",facilityService.getService().getServiceName());
                        facilityServiceInfo.put("gia",facilityService.getCost()+" USD");
                        return facilityServiceInfo;
                    }));
                    return medicalFacilityInfo;
                }));
        response.add(cosoyte);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("showCoSoYTeP2")
    public List<MedicalFacilityResponse> medicalFacilityResponseListlist(){
        List<MedicalFacility>medicalFacilityList=userFunction_service.getMedicalFacilities();
        List<MedicalFacilityResponse> medicalFacilityResponseList=new ArrayList<>();
        for (MedicalFacility medicalFacility : medicalFacilityList) {
            MedicalFacilityResponse medicalFacilityResponse = new MedicalFacilityResponse();
            medicalFacilityResponse.setName(medicalFacility.getName());
            medicalFacilityResponse.setAddress(medicalFacility.getAddress());
            medicalFacilityResponse.setPhone(medicalFacility.getPhone());
            medicalFacilityResponse.setDescription(medicalFacility.getDescription());
            medicalFacilityResponse.setEmail(medicalFacility.getEmail());
            List<String> SpecialtyName=new  ArrayList<>();
            List<FacilitySpecialty>facilitySpecialtyList=facilitySpecialtyRepository.findAllByFacility(medicalFacility);
            for (FacilitySpecialty facilitySpecialty : facilitySpecialtyList) {
                SpecialtyName.add(facilitySpecialty.getSpecialty().getName());
            }
            medicalFacilityResponse.setFacilitySpecialtyName(SpecialtyName);
            List<FacilityServiceResponse>facilityServiceResponseList=new ArrayList<>();
            List<FacilityService>serviceList=facilityServiceRepository.findFacilityServicesByFacility(medicalFacility);
            for(FacilityService facilityService : serviceList){
                FacilityServiceResponse facilityServiceResponse = new FacilityServiceResponse();
                facilityServiceResponse.setServiceName(facilityService.getService().getServiceName());
                facilityServiceResponse.setGia(facilityService.getCost()+" USD");
                facilityServiceResponseList.add(facilityServiceResponse);
            }
            medicalFacilityResponse.setFacilityServiceResponseList(facilityServiceResponseList);
            medicalFacilityResponseList.add(medicalFacilityResponse);
        }
        return medicalFacilityResponseList;
    }
}

package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Feedback;
import com.example.VNVC_1.tiemchung.model.VaccinePackage;
import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    //Cập nhật feedback rating
    @PutMapping("/{id}/rating-feedback")
    @Tag(name = "Admin cập nhật feedback rating")
    public ResponseEntity<Object> manageRatingFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        return ResponseEntity.ok(adminService.manageRatingFeedback(id, feedback));
    }
    //Cập nhật gói vaccine
    @PutMapping("/manage-vaccine-packages")
    @Tag(name ="Admin cập nhật gói vaccine")
    public ResponseEntity<Object> manageVaccinePackages(@RequestBody VaccinePackage vaccinePackage) {
        return ResponseEntity.ok(adminService.manageVaccinePackages(vaccinePackage));
    }
    //Tìm người theo role
    @GetMapping("/users-by-role/{role}")
    @Tag(name ="Admin tìm người theo role")
    public ResponseEntity<List<Users>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }
}

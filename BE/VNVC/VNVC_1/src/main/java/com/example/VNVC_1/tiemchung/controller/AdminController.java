package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Feedback;
import com.example.VNVC_1.tiemchung.model.VaccinePackage;
import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.service.AdminService;
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

    // API 9: Quản lý rating, feedback (chức năng của admin)
    @PostMapping("/{id}/rating-feedback")
    public ResponseEntity<Object> manageRatingFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        return ResponseEntity.ok(adminService.manageRatingFeedback(id, feedback));
    }

    // API 10: Quản lý bảng giá gói tiêm (chức năng của staff)
    @PutMapping("/manage-vaccine-packages")
    public ResponseEntity<Object> manageVaccinePackages(@RequestBody VaccinePackage vaccinePackage) {
        return ResponseEntity.ok(adminService.manageVaccinePackages(vaccinePackage));
    }

    // API 11: Lấy danh sách người dùng theo vai trò
    @GetMapping("/users-by-role/{role}")
    public ResponseEntity<List<Users>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }
}

package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.service.StaffService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // Lấy danh sách khách hàng
    @GetMapping("/customers")
    @Tag(name ="Staff lấy danh sách customer" )
    public ResponseEntity<List<Users>> getAllCustomers() {
        return ResponseEntity.ok(staffService.getAllCustomers());
    }

    // Tạo khách hàng mới
    @PostMapping("/customers")
    @Tag(name ="Staff tạo customer" )
    public ResponseEntity<Users> createCustomer(@RequestBody Users user) {
        return ResponseEntity.ok(staffService.createCustomer(user));
    }

    // Cập nhật thông tin khách hàng
    @PutMapping("/customers/{id}")
    @Tag(name = "Staff cập nhật customer")
    public ResponseEntity<Users> updateCustomer(@PathVariable Long id, @RequestBody Users user) {
        return ResponseEntity.ok(staffService.updateCustomer(id, user));
    }

    // Xóa khách hàng
    @DeleteMapping("/customers/{id}")
    @Tag(name ="Staff xóa customer" )
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        staffService.deleteCustomer(id);
        return ResponseEntity.ok("Khách hàng đã được xóa.");
    }
}

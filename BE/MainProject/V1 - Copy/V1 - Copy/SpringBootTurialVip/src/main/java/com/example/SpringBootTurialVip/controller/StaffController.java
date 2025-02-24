package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    //API: Xem danh sách tất cả `Child`
    @GetMapping("/children")
    public ResponseEntity<List<ChildResponse>> getAllChildren() {
        return ResponseEntity.ok(staffService.getAllChildren());
    }

    //API: Xem danh sách tất cả `Parent`
    @GetMapping("/parents")
    public ResponseEntity<List<UserResponse>> getAllParents() {
        return ResponseEntity.ok(staffService.getAllParents());
    }

    //API: Tạo `Child` cho `Parent` bất kỳ
    @PostMapping("/children/create/{parentId}")
    public ResponseEntity<ChildResponse> createChildForParent(
            @PathVariable("parentId") Long parentId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.createChildForParent(parentId, request));
    }

    //API: Cập nhật thông tin `Child`
    @PutMapping("/children/{childId}/update")
    public ResponseEntity<ChildResponse> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.updateChildInfo(childId, request));
    }
}

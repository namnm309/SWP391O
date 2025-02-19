package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Feedback;
import com.example.VNVC_1.tiemchung.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/{id}/reaction")
    @Tag(name ="user ghi phản ứng sau tiêm")
    @PutMapping("/{id}/reaction")
    @Tag(name ="user cập nhật phản ứng sau tiêm")
    @DeleteMapping("/{id}/reaction")
    @Tag(name ="user xóa phản ứng sau tiêm (nếu ghi nhầm)")
    public ResponseEntity<String> manageReaction(@PathVariable Long id, @RequestBody String reactionDetails) {
        return ResponseEntity.ok(customerService.manageReaction(id, reactionDetails));
    }

    @GetMapping("/{id}/feedback")
    @Tag(name ="user xem feedback")
    public ResponseEntity<Feedback> getFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getFeedbackByUserId(id));
    }

    @PostMapping("/{id}/feedback")
    @Tag(name ="user tạo feedback")
    @PutMapping("/{id}/feedback")
    @Tag(name ="user sửa feedback")
    @DeleteMapping("/{id}/feedback")
    @Tag(name ="user xóa feedback")
    public ResponseEntity<String> manageFeedback(@PathVariable Long id, @RequestBody Feedback feedbackDetails) {
        return ResponseEntity.ok(customerService.manageFeedback(id, feedbackDetails));
    }
}

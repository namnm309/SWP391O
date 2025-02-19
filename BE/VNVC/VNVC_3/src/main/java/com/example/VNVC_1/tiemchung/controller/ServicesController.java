package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Services;
import com.example.VNVC_1.tiemchung.model.ServicesType;
import com.example.VNVC_1.tiemchung.service.ServicesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller quản lý các API liên quan đến Service.
 */
@RestController
@RequestMapping("/api/v1")
public class ServicesController {
    private final ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    /**
     * API lấy danh sách tất cả dịch vụ từ CSDL
     * @return Danh sách các dịch vụ có trong Database
     */
    @GetMapping("/services")
    @Tag(name = "Lấy danh sách 3 gói dịch vụ")
    public ResponseEntity<List<Services>> getAllServices() {
        return ResponseEntity.ok(servicesService.getAllServices());
    }

    /**
     * API lấy thông tin dịch vụ Single từ CSDL
     * @return Dịch vụ "Single"
     */
    @GetMapping("/single")
    @Tag(name = "Gói tiêm lẻ")
    public ResponseEntity<Services> getSingleService() {
        return ResponseEntity.ok(servicesService.getServiceByType(ServicesType.SINGLE));
    }

    /**
     * API lấy thông tin dịch vụ Combo từ CSDL
     * @return Dịch vụ "Combo"
     */
    @GetMapping("/combo")
    @Tag(name = "Gói tiêm combo")
    public ResponseEntity<Services> getComboService() {
        return ResponseEntity.ok(servicesService.getServiceByType(ServicesType.COMBO));
    }

    /**
     * API lấy thông tin dịch vụ Modify từ CSDL
     * @return Dịch vụ "Modify"
     */

    @GetMapping("/modify")
    @Tag(name = "Gói tiêm tùy chỉnh")
    public ResponseEntity<Services> getModifyService() {
        return ResponseEntity.ok(servicesService.getServiceByType(ServicesType.MODIFY));
    }
}

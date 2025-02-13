package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Vaccine;
import com.example.VNVC_1.tiemchung.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vaccines")
public class VaccineController {

    private final VaccineService vaccineService;

    @Autowired
    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    // Lấy danh sách tất cả vaccines
    @GetMapping
    public List<Vaccine> getAllVaccines() {
        return vaccineService.getAllVaccines();
    }

    // Lấy vaccine theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Vaccine> getVaccineById(@PathVariable Long id) {
        Vaccine vaccine = vaccineService.getVaccineById(id);
        return ResponseEntity.ok(vaccine);
    }

    // Thêm 1 vaccine mới vào danh sách
    @PostMapping
    public ResponseEntity<Vaccine> createVaccine(@RequestBody Vaccine vaccine) {
        Vaccine createdVaccine = vaccineService.createVaccine(vaccine);
        return ResponseEntity.ok(createdVaccine);
    }

    // Cập nhật vaccine
    @PutMapping("/{id}")
    public ResponseEntity<Vaccine> updateVaccine(@PathVariable Long id, @RequestBody Vaccine vaccineDetails) {
        Vaccine updatedVaccine = vaccineService.updateVaccine(id, vaccineDetails);
        return ResponseEntity.ok(updatedVaccine);
    }

    // Xóa vaccine
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.ok("Vaccine deleted successfully");
    }
}

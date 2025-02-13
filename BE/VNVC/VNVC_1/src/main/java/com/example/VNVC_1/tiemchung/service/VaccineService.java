package com.example.VNVC_1.tiemchung.service;

import com.example.VNVC_1.tiemchung.model.Vaccine;
import com.example.VNVC_1.tiemchung.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;

    @Autowired
    public VaccineService(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    // Lấy danh sách tất cả vaccines
    public List<Vaccine> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    // Tìm vaccine theo ID
    public Vaccine getVaccineById(Long id) {
        return vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID vaccine không tìm thấy ! " + id));
    }

    // Tạo mới vaccine
    public Vaccine createVaccine(Vaccine vaccine) {
        // phải in ra 1 dòng "Nhập tên vaccine"
        return vaccineRepository.save(vaccine);
    }

    // Cập nhật vaccine
    public Vaccine updateVaccine(Long id, Vaccine vaccineDetails) {
        Vaccine vaccine = getVaccineById(id);
        vaccine.setVaccineName(vaccineDetails.getVaccineName());
        vaccine.setPrice(vaccineDetails.getPrice());
        vaccine.setDescription(vaccineDetails.getDescription());
        vaccine.setVaccineAge(vaccineDetails.getVaccineAge());
        return vaccineRepository.save(vaccine);
    }

    // Xóa vaccine
    public void deleteVaccine(Long id) {
        Vaccine vaccine = getVaccineById(id);
        vaccineRepository.delete(vaccine);
    }
}

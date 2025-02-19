package com.example.VNVC_1.tiemchung.service;

import com.example.VNVC_1.tiemchung.model.Services;
import com.example.VNVC_1.tiemchung.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    // Lấy danh sách tất cả dịch vụ
    public List<Services> getAllServices() {
        return servicesRepository.findAll();
    }

    // Tìm dịch vụ theo ID
    public Services getServiceById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ với ID: " + id));
    }

    // Tạo mới dịch vụ
    @Transactional
    public Services createService(Services service) {
        if (service.getServiceName() == null || service.getServiceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên dịch vụ không được để trống!");
        }
        if (service.getPrice() == null || service.getPrice() <= 0) {
            throw new IllegalArgumentException("Giá dịch vụ phải lớn hơn 0!");
        }
        return servicesRepository.save(service);
    }

    // Cập nhật dịch vụ
    @Transactional
    public Services updateService(Long id, Services serviceDetails) {
        Services service = getServiceById(id);
        if (serviceDetails.getServiceName() != null) {
            service.setServiceName(serviceDetails.getServiceName());
        }
        if (serviceDetails.getDescription() != null) {
            service.setDescription(serviceDetails.getDescription());
        }
        if (serviceDetails.getType() != null) {
            service.setType(serviceDetails.getType());
        }
        if (serviceDetails.getPrice() != null && serviceDetails.getPrice() > 0) {
            service.setPrice(serviceDetails.getPrice());
        }
        return servicesRepository.save(service);
    }

    // Xóa dịch vụ theo ID
    @Transactional
    public void deleteService(Long id) {
        if (!servicesRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy dịch vụ với ID: " + id);
        }
        servicesRepository.deleteById(id);
    }
}

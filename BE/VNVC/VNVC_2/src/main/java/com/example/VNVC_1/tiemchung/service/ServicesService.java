package com.example.VNVC_1.tiemchung.service;

import com.example.VNVC_1.tiemchung.model.Services;
import com.example.VNVC_1.tiemchung.model.ServicesType;
import com.example.VNVC_1.tiemchung.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service xử lý logic liên quan đến Services.
 */
@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    /**
     * Lấy danh sách tất cả dịch vụ từ CSDL.
     *
     * @return Danh sách các dịch vụ.
     */
    public List<Services> getAllServices() {
        return servicesRepository.findAll();
    }

    /**
     * Lấy thông tin dịch vụ theo loại (SINGLE, COMBO, MODIFY).
     *
     * @param type Loại dịch vụ cần lấy.
     * @return Dịch vụ tương ứng với loại được yêu cầu.
     */
    public Services getServiceByType(ServicesType type) {
        return servicesRepository.findByType(type)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dịch vụ loại: " + type));
    }
}

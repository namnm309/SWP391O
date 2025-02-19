package com.example.VNVC_1.tiemchung.service;

import com.example.VNVC_1.tiemchung.model.Feedback;
import com.example.VNVC_1.tiemchung.model.VaccinePackage;
import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.repository.AdminRepository;
import com.example.VNVC_1.tiemchung.repository.VaccinePackageRepository;
import com.example.VNVC_1.tiemchung.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final VaccinePackageRepository vaccinePackageRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, VaccinePackageRepository vaccinePackageRepository, UsersRepository usersRepository) {
        this.adminRepository = adminRepository;
        this.vaccinePackageRepository = vaccinePackageRepository;
        this.usersRepository = usersRepository;
    }

    public String manageRatingFeedback(Long userId, Feedback feedback) {
        feedback.setUserId(userId);
        adminRepository.save(feedback);
        return "Feedback của user ID " + userId + " đã được cập nhật.";
    }

    public String manageVaccinePackages(VaccinePackage vaccinePackage) {
        vaccinePackageRepository.save(vaccinePackage);
        return "Bảng giá gói tiêm đã được cập nhật.";
    }

    public List<Users> getUsersByRole(String role) {
        return usersRepository.findByRole(role);
    }
}

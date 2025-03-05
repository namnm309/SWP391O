package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.StaffUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.repository.NotificationRepository;
import com.example.SpringBootTurialVip.service.AdminDashboardService;
import com.example.SpringBootTurialVip.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final ProductOrderRepository productOrderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public Double getAverageDailyOrders() {
        return productOrderRepository.getAverageDailyOrders();
    }

    @Override
    public String getTopVaccineOfMonth() {
        return productOrderRepository.getTopVaccineOfMonth();
    }

    @Override
    public RevenueResponse getRevenue(String startDate, String endDate) {
        // Chuyển đổi String sang LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return productOrderRepository.getRevenue(start, end);
    }

    @Override
    public Integer getMostVaccinatedAge() {
        return productOrderRepository.getMostVaccinatedAge();
    }

    @Override
    public List<User> getStaffList() {
        return userRepository.findByRoles_Name("STAFF");
    }

//    @Override
//    public void addStaff(User staff) {
//        Role rolename=staff.getRoles();
//        staff.setRoles(rolename.setName());
//        userRepository.save(staff);
//    }

    @Override
    public void updateStaff(Long id, StaffUpdateRequest staff) {
        User existingStaff = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
        existingStaff.setFullname(staff.getFullname());
        existingStaff.setEnabled(staff.isEnabled());
        existingStaff.setRoles(staff.getRoles());
        userRepository.save(existingStaff);
    }

    @Override
    public void sendNotificationToStaff(String message) {
        List<User> staffList = userRepository.findByRoles_Name("STAFF");
        for (User staff : staffList) {
            notificationService.sendNotification(staff.getId(), message);
        }
    }
}

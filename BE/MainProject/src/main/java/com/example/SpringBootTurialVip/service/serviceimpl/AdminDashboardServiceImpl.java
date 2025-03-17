package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.StaffUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.OrderDetailRepository;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.repository.NotificationRepository;
import com.example.SpringBootTurialVip.service.AdminDashboardService;
import com.example.SpringBootTurialVip.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public Double getAverageDailyOrders() {
        return orderDetailRepository.getAverageDailyOrders();
    }

    @Override
    public String getTopVaccineOfMonth() {
        return orderDetailRepository.getTopVaccineOfMonth();
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


    //
//    @Override
//    public long getNewCustomersLast7Days() {
//        return userRepository.countNewCustomersSince(LocalDateTime.now().minusDays(7));
//    }
//
//    @Override
//    public long getNewCustomersLast30Days() {
//        return userRepository.countNewCustomersSince(LocalDateTime.now().minusDays(30));
//    }
//
//    @Override
//    public long getNewCustomersLast90Days() {
//        return userRepository.countNewCustomersSince(LocalDateTime.now().minusDays(90));
//    }
//
//    @Override
//    public long getTotalCustomers() {
//        return userRepository.countTotalCustomers();
//    }
//
//    @Override
//    public Map<String, Long> getCustomerGrowthStats() {
//        Map<String, Long> stats = new HashMap<>();
//        stats.put("last_7_days", getNewCustomersLast7Days());
//        stats.put("last_30_days", getNewCustomersLast30Days());
//        stats.put("last_90_days", getNewCustomersLast90Days());
//        return stats;
//    }
//
//
//
//
//
//
//    private Map<String, Object> getTopVaccineSince(LocalDate startDate) {
//        Object[] result = productOrderRepository.findTopVaccineSince(startDate);
//        Map<String, Object> data = new HashMap<>();
//        if (result != null) {
//            data.put("vaccineName", result[0]);
//            data.put("totalDoses", result[1]);
//        } else {
//            data.put("vaccineName", "No Data");
//            data.put("totalDoses", 0);
//        }
//        return data;
//    }
//
//    @Override
//    public Map<String, Object> getTopVaccineLast7Days() {
//        return getTopVaccineSince(LocalDate.now().minusDays(7));
//    }
//
//    @Override
//    public Map<String, Object> getTopVaccineLast30Days() {
//        return getTopVaccineSince(LocalDate.now().minusDays(30));
//    }
//
//    @Override
//    public Map<String, Object> getTopVaccineLast90Days() {
//        return getTopVaccineSince(LocalDate.now().minusDays(90));
//    }
//
//    @Override
//    public Map<String, Map<String, Object>> getTopVaccinesStats() {
//        Map<String, Map<String, Object>> stats = new HashMap<>();
//        stats.put("last_7_days", getTopVaccineLast7Days());
//        stats.put("last_30_days", getTopVaccineLast30Days());
//        stats.put("last_90_days", getTopVaccineLast90Days());
//        return stats;
//    }
//
//
//
//
//
//
//    private Double getRevenueSince(LocalDate startDate) {
//        return productOrderRepository.getRevenueSince(startDate);
//    }
//
//    @Override
//    public Double getRevenueLast7Days() {
//        return getRevenueSince(LocalDate.now().minusDays(7));
//    }
//
//    @Override
//    public Double getRevenueLast30Days() {
//        return getRevenueSince(LocalDate.now().minusDays(30));
//    }
//
//    @Override
//    public Double getRevenueLast90Days() {
//        return getRevenueSince(LocalDate.now().minusDays(90));
//    }
//
//    @Override
//    public Map<String, Double> getRevenueStats() {
//        Map<String, Double> stats = new HashMap<>();
//        stats.put("last_7_days", getRevenueLast7Days());
//        stats.put("last_30_days", getRevenueLast30Days());
//        stats.put("last_90_days", getRevenueLast90Days());
//        return stats;
//    }
//
//    private Map<String, Object> getMostVaccinatedAgeSince(LocalDate startDate) {
//        Object[] result = productOrderRepository.findMostVaccinatedAgeSince(startDate);
//        Map<String, Object> data = new HashMap<>();
//        if (result != null) {
//            data.put("age", result[0]);
//            data.put("totalDoses", result[1]);
//        } else {
//            data.put("age", "No Data");
//            data.put("totalDoses", 0);
//        }
//        return data;
//    }
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public Map<String, Object> getMostVaccinatedAgeLast7Days() {
//        return getMostVaccinatedAgeSince(LocalDate.now().minusDays(7));
//    }
//
//    @Override
//    public Map<String, Object> getMostVaccinatedAgeLast30Days() {
//        return getMostVaccinatedAgeSince(LocalDate.now().minusDays(30));
//    }
//
//    @Override
//    public Map<String, Object> getMostVaccinatedAgeLast90Days() {
//        return getMostVaccinatedAgeSince(LocalDate.now().minusDays(90));
//    }
//
//    @Override
//    public Map<String, Map<String, Object>> getMostVaccinatedAgeStats() {
//        Map<String, Map<String, Object>> stats = new HashMap<>();
//        stats.put("last_7_days", getMostVaccinatedAgeLast7Days());
//        stats.put("last_30_days", getMostVaccinatedAgeLast30Days());
//        stats.put("last_90_days", getMostVaccinatedAgeLast90Days());
//        return stats;
//    }
//
//
//
//
//
//
//
//
//    private Map<String, Object> getLeastOrderedVaccineSince(LocalDate startDate) {
//        Object[] result = productOrderRepository.findLeastOrderedVaccineSince(startDate);
//        Map<String, Object> data = new HashMap<>();
//        if (result != null) {
//            data.put("vaccineName", result[0]);
//            data.put("totalDoses", result[1]);
//        } else {
//            data.put("vaccineName", "No Data");
//            data.put("totalDoses", 0);
//        }
//        return data;
//    }
//
//    @Override
//    public Map<String, Object> getLeastOrderedVaccineLast7Days() {
//        return getLeastOrderedVaccineSince(LocalDate.now().minusDays(7));
//    }
//
//    @Override
//    public Map<String, Object> getLeastOrderedVaccineLast30Days() {
//        return getLeastOrderedVaccineSince(LocalDate.now().minusDays(30));
//    }
//
//    @Override
//    public Map<String, Object> getLeastOrderedVaccineLast90Days() {
//        return getLeastOrderedVaccineSince(LocalDate.now().minusDays(90));
//    }
//
//    @Override
//    public Map<String, Map<String, Object>> getLeastOrderedVaccinesStats() {
//        Map<String, Map<String, Object>> stats = new HashMap<>();
//        stats.put("last_7_days", getLeastOrderedVaccineLast7Days());
//        stats.put("last_30_days", getLeastOrderedVaccineLast30Days());
//        stats.put("last_90_days", getLeastOrderedVaccineLast90Days());
//        return stats;
//    }
//
//
//
//
//
//
//
//
//
//
//    public Map<String, Object> getAllDashboardStats() {
//        return Map.of(
//                "customerGrowth", getCustomerGrowthStats(),
//                "topVaccines", getTopVaccinesStats(),
//                "leastOrderedVaccines", getLeastOrderedVaccinesStats(),
//                "revenueStats", getRevenueStats(),
//                "mostVaccinatedAge", getMostVaccinatedAgeStats()
//        );
//    }
//
//}





    //Bản này ổn rồi nhưng không ghi rõ ngày

//    @Override
//    public Map<LocalDate, Long> getDailyNewCustomers(int days) {
//        return fetchDataWithDefault(userRepository.getDailyNewCustomers(days), 0L);
//    }
//
//    @Override
//    public Map<LocalDate, Double> getDailyRevenue(int days) {
//        return fetchDataWithDefault(productOrderRepository.getDailyRevenue(days), 0.0);
//    }
//
//    @Override
//    public Map<LocalDate, Map<String, Object>> getDailyTopVaccine(int days) {
//        return fetchComplexData(productOrderRepository.getDailyTopVaccine(days));
//    }
//
//    @Override
//    public Map<LocalDate, Map<String, Object>> getDailyLeastOrderedVaccine(int days) {
//        return fetchComplexData(productOrderRepository.getDailyLeastOrderedVaccine(days));
//    }
//
//    @Override
//    public Map<LocalDate, Map<String, Object>> getDailyMostVaccinatedAge(int days) {
//        return fetchComplexData(productOrderRepository.getDailyMostVaccinatedAge(days));
//    }

    private <T> Map<LocalDate, T> fetchDataWithDefault(List<Object[]> results, T defaultValue) {
        Map<LocalDate, T> data = new LinkedHashMap<>();
        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            T value = (T) row[1];
            data.put(date, value);
        }
        return data;
    }

    private Map<LocalDate, Map<String, Object>> fetchComplexData(List<Object[]> results) {
        Map<LocalDate, Map<String, Object>> data = new LinkedHashMap<>();
        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            String key = row[1].toString();
            Long value = ((Number) row[2]).longValue();
            data.put(date, Map.of(key, value));
        }
        return data;
    }







    @Override
    public Map<LocalDate, Long> getDailyNewCustomers(int days) {
        return fillMissingDates(userRepository.getDailyNewCustomers(days), days, 0L);
    }


    @Override
    public Map<LocalDate, Double> getDailyRevenue(int days) {
        return fillMissingDates(productOrderRepository.getDailyRevenue(days), days, 0.0);
    }


    @Override
    public Map<LocalDate, Map<String, Object>> getDailyTopVaccine(int days) {
        return fillMissingDatesWithEmptyMap(productOrderRepository.getDailyTopVaccine(days), days);
    }


    @Override
    public Map<LocalDate, Map<String, Object>> getDailyLeastOrderedVaccine(int days) {
        return fillMissingDatesWithEmptyMap(productOrderRepository.getDailyLeastOrderedVaccine(days), days);
    }



    @Override
    public Map<LocalDate, Map<String, Object>> getDailyMostVaccinatedAge(int days) {
        return fillMissingDatesWithEmptyMap(productOrderRepository.getDailyMostVaccinatedAge(days), days);
    }






    private <T> Map<LocalDate, T> fillMissingDates(List<Object[]> results, int days, T defaultValue) {
        Map<LocalDate, T> fullDateRange = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        // Mặc định gán giá trị 0 cho tất cả các ngày
        for (int i = 0; i < days; i++) {
            fullDateRange.put(today.minusDays(i), defaultValue);
        }

        // Cập nhật giá trị từ database
        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            T value = (T) row[1];
            fullDateRange.put(date, value);
        }

        return fullDateRange;
    }



    private Map<LocalDate, Map<String, Object>> fillMissingDatesWithEmptyMap(List<Object[]> results, int days) {
        Map<LocalDate, Map<String, Object>> fullDateRange = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        // Mặc định gán giá trị `{}` cho tất cả các ngày
        for (int i = 0; i < days; i++) {
            fullDateRange.put(today.minusDays(i), new HashMap<>());
        }

        // Cập nhật giá trị từ database
        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            String key = row[1].toString();
            Long value = ((Number) row[2]).longValue();
            fullDateRange.put(date, Map.of(key, value));
        }

        return fullDateRange;
    }



}


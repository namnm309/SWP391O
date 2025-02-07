package com.example.VNVC_1.tiemchung.service;

// Import các model và repository cần thiết
import com.example.VNVC_1.tiemchung.model.Feedback;
import com.example.VNVC_1.tiemchung.model.RoleUsers;
import com.example.VNVC_1.tiemchung.model.VaccinePackage;
import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.repository.FeedbackRepository;
import com.example.VNVC_1.tiemchung.repository.VaccinePackageRepository;
import com.example.VNVC_1.tiemchung.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Annotation @Service đánh dấu class này là một Service của Spring Boot.
 * Spring sẽ tự động quản lý nó như một Bean trong ApplicationContext.
 */
@Service
public class AdminService {

    // Khai báo repository để tương tác với database
    private final FeedbackRepository feedbackRepository;
    private final VaccinePackageRepository vaccinePackageRepository;
    private final UsersRepository usersRepository;

    /**
     * Constructor của AdminService, sử dụng @Autowired để Spring tự động Inject các Repository.
     *
     * @param feedbackRepository Repository để thao tác với bảng Feedback.
     * @param vaccinePackageRepository Repository để thao tác với bảng VaccinePackage.
     * @param usersRepository Repository để thao tác với bảng Users.
     */
    @Autowired
    public AdminService(FeedbackRepository feedbackRepository, VaccinePackageRepository vaccinePackageRepository, UsersRepository usersRepository) {
        this.feedbackRepository = feedbackRepository;
        this.vaccinePackageRepository = vaccinePackageRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * Quản lý đánh giá và phản hồi của người dùng (Admin có quyền cập nhật feedback).
     *
     * @param userId ID của người dùng gửi feedback.
     * @param feedback Đối tượng Feedback chứa nội dung phản hồi.
     * @return Thông báo xác nhận feedback đã được cập nhật.
     */
    public String manageRatingFeedback(Long userId, Feedback feedback) {
        feedback.setUserId(userId); // Gán ID người dùng cho feedback trước khi lưu
        feedbackRepository.save(feedback); // Lưu feedback vào database
        return "Feedback của user ID " + userId + " đã được cập nhật.";
    }

    /**
     * Quản lý bảng giá gói tiêm (Admin có thể cập nhật thông tin gói tiêm).
     *
     * @param vaccinePackage Đối tượng VaccinePackage chứa thông tin gói tiêm.
     * @return Thông báo xác nhận bảng giá gói tiêm đã được cập nhật.
     */
    public String manageVaccinePackages(VaccinePackage vaccinePackage) {
        vaccinePackageRepository.save(vaccinePackage); // Lưu thông tin gói tiêm vào database
        return "Bảng giá gói tiêm đã được cập nhật.";
    }

    /**
     * Lấy danh sách người dùng theo vai trò (Role).
     *
     * @param role Vai trò của người dùng dưới dạng String (ví dụ: "ADMIN", "STAFF", "CUSTOMER").
     * @return Danh sách người dùng có vai trò tương ứng.
     */
    public List<Users> getUsersByRole(String role) {
        try{
        // Role trong UsersRepository là kiểu Enum nhưng giá trị truyền vào là String, nên cần chuyển đổi.
        RoleUsers roleEnum = RoleUsers.valueOf(role.toUpperCase()); // Chuyển đổi từ String sang Enum RoleUsers
        return usersRepository.findByRole(roleEnum);// Truy vấn danh sách người dùng có vai trò tương ứng
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Role không hợp lệ: " + role);
    }
    }
}

package com.example.VNVC_1.tiemchung.service;

// Import các class cần thiết
import com.example.VNVC_1.tiemchung.model.Feedback;
import com.example.VNVC_1.tiemchung.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Annotation @Service đánh dấu class này là một Spring Service.
 * Spring sẽ tự động quản lý nó như một Bean trong ApplicationContext.
 */
@Service
public class CustomerService {

    // Khai báo FeedbackRepository để làm việc với database
    private final FeedbackRepository feedbackRepository;

    /**
     * Constructor của CustomerService, sử dụng @Autowired để Spring tự động Inject FeedbackRepository.
     *
     * @param feedbackRepository Repository để thao tác với bảng Feedback trong Database.
     */
    @Autowired
    public CustomerService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Xử lý cập nhật phản ứng sau tiêm của một user.
     *
     * @param userId ID của người dùng cần cập nhật phản ứng sau tiêm.
     * @param reactionDetails Nội dung phản ứng sau tiêm.
     * @return Thông báo xác nhận phản ứng sau tiêm đã được cập nhật.
     */
    public String manageReaction(Long userId, String reactionDetails) {
        return "Phản ứng sau tiêm của user ID " + userId + " đã được cập nhật.";
    }

    /**
     * Lấy feedback của một user dựa vào userId.
     *
     * @param userId ID của user cần lấy feedback.
     * @return Đối tượng Feedback nếu tồn tại, nếu không trả về null.
     */
    public Feedback getFeedbackByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId).orElse(null);
    }

    /**
     * Cập nhật hoặc thêm mới feedback của một user.
     *
     * @param userId ID của user gửi feedback.
     * @param feedback Đối tượng Feedback chứa nội dung feedback.
     * @return Thông báo xác nhận feedback đã được cập nhật.
     */
    public String manageFeedback(Long userId, Feedback feedback) {
        feedback.setUserId(userId); // Gán userId cho feedback trước khi lưu
        feedbackRepository.save(feedback); // Lưu feedback vào database
        return "Feedback của user ID " + userId + " đã được cập nhật.";
    }
}

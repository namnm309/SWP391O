package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    // Khách hàng gửi đánh giá
    public Feedback submitFeedback(Long userId, int rating, String comment);

    // Khách hàng xem feedback của mình
    public List<Feedback> getFeedbackByUser(Long userId);

    // Khách hàng sửa đánh giá
    public Feedback submitOrUpdateFeedback(Long userId, int rating, String comment);

    // Khách hàng xóa đánh giá
    public void deleteFeedbackByUser(Long userId);

    // Staff phản hồi feedback
    public Feedback replyFeedback(Long feedbackId, String reply, Long userId);

    // Lấy danh sách feedback chưa phản hồi
    public List<Feedback> getUnrepliedFeedbacks();

    // Admin xem feedback theo số sao
    public List<Feedback> getFeedbackByRating(int rating);

    // Admin lấy số sao trung bình
    public Double getAverageRating();

    //Xem all đánh gí từ 5 sao đến 1 sao
    public List<Feedback> getFeedbacksSortedByRatingDesc();

    // Lấy đánh giá từ 1 sao đến 5 sao
    public List<Feedback> getFeedbacksSortedByRatingAsc();

    public List<Feedback> getAllFeedbacks();

}

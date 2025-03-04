package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.entity.Feedback;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.FeedbackRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedBackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Feedback submitFeedback(Long userId, int rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getFeedbackByUser(Long userId) {
        return feedbackRepository.findAll().stream()
                .filter(feedback -> feedback.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Feedback submitOrUpdateFeedback(Long userId, int rating, String comment) {
        Optional<Feedback> existingFeedback = feedbackRepository.findByUserId(userId);

        if (existingFeedback.isPresent()) {
            Feedback feedback = existingFeedback.get();
            feedback.setRating(rating);
            feedback.setComment(comment);
            feedback.setUpdatedAt(LocalDateTime.now());
            return feedbackRepository.save(feedback);
        } else {
            Feedback newFeedback = new Feedback();
            newFeedback.setId(userId);
            newFeedback.setRating(rating);
            newFeedback.setComment(comment);
            newFeedback.setCreatedAt(LocalDateTime.now());
            newFeedback.setUpdatedAt(LocalDateTime.now());
            return feedbackRepository.save(newFeedback);
        }
    }


    @Override
    public void deleteFeedbackByUser(Long userId) {
        feedbackRepository.deleteByUserId(userId);
    }

    @Override
    public Feedback replyFeedback(Long feedbackId, String reply, Long userId) {
        return feedbackRepository.findById(feedbackId).map(feedback -> {
            feedback.setStaffReply(reply);
            feedback.setReplied(true); // Đánh dấu đã phản hồi
            feedback.setUpdatedAt(LocalDateTime.now());
            return feedbackRepository.save(feedback);
        }).orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    @Override
    public List<Feedback> getUnrepliedFeedbacks() {
        return feedbackRepository.findByRepliedFalse();
    }

    @Override
    public List<Feedback> getFeedbackByRating(int rating) {
        return feedbackRepository.findByRating(rating);
    }

    @Override
    public Double getAverageRating() {
        return feedbackRepository.findAverageRating();
    }

    @Override
    public List<Feedback> getFeedbacksSortedByRatingDesc() {
        return feedbackRepository.findAllByOrderByRatingDesc();
    }

    @Override
    public List<Feedback> getFeedbacksSortedByRatingAsc() {
        return feedbackRepository.findAllByOrderByRatingAsc();
    }
}

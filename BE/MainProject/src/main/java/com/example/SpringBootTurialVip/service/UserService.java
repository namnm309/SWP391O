package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.constant.PredefinedRole;
import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public interface UserService {
    public User createUser(UserCreationRequest request);

    //Method xac thuc account de cho phep dang nhap
    public void verifyUser(VerifyAccountRequest verifyAccountRequest);

    //Method cho phep gui lai ma code
    public void resendVerificationCode(String email);

    //Method gui email
//    private void sendVerificationEmail(User user) {
//
//    }

    //Method tạo mã xác thực
//    private String generateVerificationCode();

    //Danh sách user
    public List<User> getUsers();

    //Kiếm user băằng ID
    public UserResponse getUserById(Long id);

    //Lấy thông tin hiện tại đang log in
    public UserResponse getMyInfo();

    //Cập nhật thông tin ver cũ
    public UserResponse updateUser(Long userId, UserUpdateRequest request);

    //Cập nhật thông tin ver mới
    public User updateUser(User user);

    //Method cập nhật lại mk = token mới
    public User updateUserByResetToken(User user);

    //Xóa user
    public void deleteUser(Long userId);

    //Tạo hồ sơ trẻ
    //Tạo tài khoản
    public User createChild(ChildCreationRequest request);

    //Method lấy thông tin trẻ của người dùng
    public List<ChildResponse> getChildInfo();

    //Method cập nhật thông tin trẻ
    public ChildResponse updateChildrenByParent(ChildUpdateRequest request);

    //method find user = mail
    public User getUserByEmail(String email);

    //Method này gửi resetToken về mail để sign lại mk mới
    public void updateUserResetToken(String email, String resetToken);

    //Method dựa trên token để find ra user
    public User getUserByToken(String token);

    //Tìm user = username
    public Optional<User> getUserByUsername(String username);

}

package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public interface StaffService {

    public List<ChildResponse> getAllChildren();

    // Lấy danh sách tất cả `Parent
    public List<UserResponse> getAllParents();

    // Tạo `Child` cho `Parent` bất kỳ
    public ChildResponse createChildForParent(Long parentId,
                                              ChildCreationRequest request,
                                              MultipartFile avatar);

    // Cập nhật thông tin `Child` của bất kỳ `Parent`
    public ChildResponse updateChildInfo(Long childId,
                                         ChildCreationRequest request,
                                         MultipartFile avatar);
}

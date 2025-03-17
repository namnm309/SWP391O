package com.example.SpringBootTurialVip.service.serviceimpl;


import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.entity.Permission;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.mapper.PermissionMapper;
import com.example.SpringBootTurialVip.repository.PermissionRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PermissionMapper permissionMapper;


    @Autowired
    UserRepository userRepository;



    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request) {
//        Permission permission = permissionMapper.toPermission(request);
//        permission = permissionRepository.save(permission);
//        return permissionMapper.toPermissionResponse(permission);

        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Permission name cannot be null or empty");
        }

        Permission permission = permissionMapper.toPermission(request);

        if (permissionRepository.existsById(permission.getName())) {
            throw new IllegalArgumentException("Permission already exists!");
        }

        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }



    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUserPermissions(Long userId, List<String> permissionNames) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        // Lấy danh sách quyền từ database
        List<Permission> permissions = permissionRepository.findAllById(permissionNames);

        if (permissions.size() != permissionNames.size()) {
            throw new IllegalArgumentException("Một số quyền không tồn tại trong hệ thống");
        }

        // Gán lại danh sách quyền cho user
        user.setPermissions(new HashSet<>(permissions));

        // Lưu lại user
        userRepository.save(user);
    }

}

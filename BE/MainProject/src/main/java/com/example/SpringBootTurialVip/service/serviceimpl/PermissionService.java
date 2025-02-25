package com.example.SpringBootTurialVip.service.serviceimpl;


import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.entity.Permission;
import com.example.SpringBootTurialVip.mapper.PermissionMapper;
import com.example.SpringBootTurialVip.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PermissionMapper permissionMapper;



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

    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}

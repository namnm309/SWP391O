package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    public PermissionResponse create(PermissionRequest request);

    public List<PermissionResponse> getAll();

    public void delete(String permission);

    public void updateUserPermissions(Long userId, List<String> permissions);

}

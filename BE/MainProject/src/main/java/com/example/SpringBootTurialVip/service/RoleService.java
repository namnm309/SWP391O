package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.RoleRequest;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    public RoleResponse create(RoleRequest request);

    public List<RoleResponse> getAll();

    public void delete(String role);

    public void removePermissionFromRole(String roleName, String permissionName);
}

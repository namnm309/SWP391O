package com.example.SpringBootTurialVip.mapper;

import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}

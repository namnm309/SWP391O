package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.RoleRequest;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.entity.Permission;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.mapper.RoleMapper;
import com.example.SpringBootTurialVip.repository.PermissionRepository;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleMapper roleMapper;

//    public RoleResponse create(RoleRequest request) {
//        var role = roleMapper.toRole(request);
//
//        var permissions = permissionRepository.findAllById(request.getPermissions());
//        role.setPermissions(new HashSet<>(permissions));
//
//        role = roleRepository.save(role);
//        return roleMapper.toRoleResponse(role);
//    }

    public RoleResponse create(RoleRequest request) {
        log.info("🔹 Received RoleRequest: {}", request);

        // Chuyển đổi RoleRequest thành Role entity
        var role = roleMapper.toRole(request);
        log.info("🔹 Converted Role: {}", role);

        // Tìm danh sách Permission theo ID từ request
        var permissions = permissionRepository.findAllById(request.getPermissions());
        log.info("🔹 Permissions retrieved from DB: {}", permissions);

        // Gán permissions vào Role
        role.setPermissions(new HashSet<>(permissions));
        log.info("🔹 Role after setting permissions: {}", role);

        // Lưu Role vào database
        role = roleRepository.save(role);
        log.info("✅ Role saved to DB: {}", role);

        // Chuyển đổi Role đã lưu thành RoleResponse
        var roleResponse = roleMapper.toRoleResponse(role);
        log.info("✅ Returning RoleResponse: {}", roleResponse);

        return roleResponse;
    }

//public Role create(RoleRequest roleRequest) {
//    Role role = new Role();
//    role.setName(roleRequest.getName());
//    role.setDescription(roleRequest.getDescription());
//
//    // Lấy danh sách permissions từ database
//    List<Permission> permissions = permissionRepository.findAllById(roleRequest.getPermissions());
//
//    // Kiểm tra nếu permissions bị null
//    if (permissions.isEmpty()) {
//        throw new RuntimeException("Permissions không tồn tại hoặc chưa được tạo!");
//    }
//
//    // Gán permissions vào role
//    role.setPermissions(new HashSet<>(permissions));
//
//    // Lưu role vào DB
//    return roleRepository.save(role);
//}



    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}

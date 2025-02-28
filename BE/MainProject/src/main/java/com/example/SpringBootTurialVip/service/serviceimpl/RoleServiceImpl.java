package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.RoleRequest;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.entity.Permission;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.mapper.RoleMapper;
import com.example.SpringBootTurialVip.repository.PermissionRepository;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import com.example.SpringBootTurialVip.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
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
    @Override
    public RoleResponse create(RoleRequest request) {
        log.info("Received RoleRequest: {}", request);

        // Chuyển đổi RoleRequest thành Role entity
        var role = roleMapper.toRole(request);
        log.info("Converted Role: {}", role);

        // Tìm danh sách Permission theo ID từ request
        var permissions = permissionRepository.findAllById(request.getPermissions());
        log.info("Permissions retrieved from DB: {}", permissions);

        // Gán permissions vào Role
        role.setPermissions(new HashSet<>(permissions));
        log.info("Role after setting permissions: {}", role);

        // Lưu Role vào database
        role = roleRepository.save(role);
        log.info("Role saved to DB: {}", role);

        // Chuyển đổi Role đã lưu thành RoleResponse
        var roleResponse = roleMapper.toRoleResponse(role);
        log.info("Returning RoleResponse: {}", roleResponse);

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
//
//
//
    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
//
//    // Kiểm tra xem name có hợp lệ theo enum Role không
//    public boolean isValidRole(String name) {
//        return Arrays.stream(Role.values())
//                .anyMatch(role -> role.name().equals(name));
//    }
//
//    // Kiểm tra danh sách permissions
//    public List<String> validatePermissions(List<String> permissions) {
//        List<String> invalidPermissions = new ArrayList<>();
//        for (String permission : permissions) {
//            if (!permissionRepository.existsByName(permission)) {
//                invalidPermissions.add(permission);
//            }
//        }
//        return invalidPermissions;
//    }
//
//    // Tạo role mới nếu dữ liệu hợp lệ
//    public RoleResponse create(RoleRequest request) {
//        // Kiểm tra xem role name có hợp lệ không
//        if (!isValidRole(request.getName())) {
//            throw new IllegalArgumentException("Error: Role name must be ADMIN, STAFF, or CUSTOMER");
//        }
//
//        // Kiểm tra danh sách permissions
//        List<String> invalidPermissions = validatePermissions(request.getPermissions());
//        if (!invalidPermissions.isEmpty()) {
//            throw new IllegalArgumentException("Error: Invalid permissions found - " + invalidPermissions);
//        }
//
//        // Tạo role mới
//        RoleEntity role = new RoleEntity();
//        role.setName(request.getName());
//        role.setDescription(request.getDescription());
//
//        // Gán permissions cho role
//        List<Permission> validPermissions = permissionRepository.findByNameIn(request.getPermissions());
//        role.setPermissions(validPermissions);
//
//        // Lưu vào database
//        roleRepository.save(role);
//
//        return new RoleResponse(role);
//    }

    @Override
    public void removePermissionFromRole(String roleName, String permissionName) {
        // Tìm role theo tên
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + roleName));

        // Tìm permission theo tên
        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new NoSuchElementException("Permission not found: " + permissionName));

        // Kiểm tra nếu permission có trong role
        if (!role.getPermissions().contains(permission)) {
            throw new IllegalStateException("Permission does not exist in this role.");
        }

        // Xóa permission khỏi role
        role.getPermissions().remove(permission);

        // Lưu thay đổi vào database
        roleRepository.save(role);
    }

}

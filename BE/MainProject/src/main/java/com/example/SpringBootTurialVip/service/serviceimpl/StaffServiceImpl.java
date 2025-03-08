package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.entity.UserRelationship;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.mapper.UserMapper;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import com.example.SpringBootTurialVip.repository.UserRelationshipRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.StaffService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    //@Autowired
    private final UserRepository userRepository;

    //@Autowired
    private final UserMapper userMapper;

    //@Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private UserRelationshipRepository userRelationshipRepository;



    // Lấy danh sách tất cả `Child` (Không giới hạn Parent)
    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public List<ChildResponse> getAllChildren() {
        List<User> children = userRepository.findByParentidIsNotNull(); // Lấy tất cả Child
        return children.stream().map(userMapper::toChildResponse).collect(Collectors.toList());
    }

    // Lấy danh sách tất cả `Parent`
    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public List<UserResponse> getAllParents() {
        List<User> parents = userRepository.findByParentidIsNull(); // Lấy tất cả Parent
        return parents.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    // Tạo `Child` cho `Parent` bất kỳ
    @Override
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ChildResponse createChildForParent(Long parentId, ChildCreationRequest request) {
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        User child = userMapper.toUser(request);
        child.setParentid(parent.getId());

        // Gán Role `ROLE_CHILD`
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById("ROLE_CHILD").ifPresent(roles::add);
        child.setRoles(roles);
        child.setEnabled(true);

        userRepository.save(child);
        return userMapper.toChildResponse(child);
    }

    // Cập nhật thông tin `Child` của bất kỳ `Parent`
    @Override
    // @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    // API cập nhật thông tin trẻ
    @Transactional
    public ChildResponse updateChildInfo(Long childId, ChildCreationRequest request) {
        // Lấy thông tin trẻ từ DB
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new AppException(ErrorCode.CHILD_NOT_FOUND));

        // Lấy người dùng hiện tại từ SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User editor = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Kiểm tra quyền chỉnh sửa (chỉ cha mẹ hoặc staff được chỉnh sửa)
//        if (!editor.getId().equals(child.getParentid()) && !editor.hasRole("STAFF")) {
//            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
//        }

        // Cập nhật thông tin trẻ
        child.setFullname(request.getFullname());
        child.setBod(request.getBod());
        child.setGender(request.getGender());
        child.setHeight(request.getHeight());
        child.setWeight(request.getWeight());

        // Cập nhật hoặc thêm mới người thân
        if (request.getRelationshipType() != null) {
            UserRelationship relationship = userRelationshipRepository.findByChildAndRelative(child, editor)
                    .orElse(new UserRelationship(child, editor, request.getRelationshipType()));

            relationship.setRelationshipType(request.getRelationshipType());
            userRelationshipRepository.save(relationship);
        }

        // Lưu lại thay đổi
        child = userRepository.save(child);

        // Lấy danh sách quan hệ hiện tại của trẻ
        List<UserRelationship> relationships = userRelationshipRepository.findByChild(child);

        return new ChildResponse(child, relationships);
    }


}

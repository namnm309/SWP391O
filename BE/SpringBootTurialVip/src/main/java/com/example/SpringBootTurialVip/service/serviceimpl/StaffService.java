package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ChildCreationRequest;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.mapper.UserMapper;
import com.example.SpringBootTurialVip.repository.RoleRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {
    //@Autowired
    private final UserRepository userRepository;

    //@Autowired
    private final UserMapper userMapper;

    //@Autowired
    private final RoleRepository roleRepository;



    // Lấy danh sách tất cả `Child` (Không giới hạn Parent)
    @PreAuthorize("hasRole('STAFF')")
    public List<ChildResponse> getAllChildren() {
        List<User> children = userRepository.findByParentidIsNotNull(); // Lấy tất cả Child
        return children.stream().map(userMapper::toChildResponse).collect(Collectors.toList());
    }

    // Lấy danh sách tất cả `Parent`
    @PreAuthorize("hasRole('STAFF')")
    public List<UserResponse> getAllParents() {
        List<User> parents = userRepository.findByParentidIsNull(); // Lấy tất cả Parent
        return parents.stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    // Tạo `Child` cho `Parent` bất kỳ
    @PreAuthorize("hasRole('STAFF')")
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
    @PreAuthorize("hasRole('STAFF')")
    public ChildResponse updateChildInfo(Long childId, ChildCreationRequest request) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new AppException(ErrorCode.CHILD_NOT_EXISTED));

        child.setFullname(request.getFullname());
        child.setBod(request.getBod());
        child.setGender(request.getGender());
        child.setHeight(request.getHeight());
        child.setWeight(request.getWeight());

        userRepository.save(child);
        return userMapper.toChildResponse(child);
    }
}

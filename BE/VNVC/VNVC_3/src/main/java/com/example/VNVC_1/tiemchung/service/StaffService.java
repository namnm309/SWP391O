package com.example.VNVC_1.tiemchung.service;

import com.example.VNVC_1.tiemchung.model.RoleUsers;
import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StaffService {
    private final UsersRepository usersRepository;

    @Autowired
    public StaffService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Lấy danh sách khách hàng
    public List<Users> getAllCustomers() {
        return usersRepository.findByRole(RoleUsers.CUSTOMER);
    }

    // Tạo khách hàng mới
    public Users createCustomer(Users user) {
        user.setRole(RoleUsers.CUSTOMER);  // Sử dụng Enum thay vì String
        return usersRepository.save(user);
    }

    // Cập nhật thông tin khách hàng
    public Users updateCustomer(Long id, Users userDetails) {
        Users user = getUserById(id);
        user.setFullname(userDetails.getFullname());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        return usersRepository.save(user);
    }

    // Xóa khách hàng
    public void deleteCustomer(Long id) {
        usersRepository.deleteById(id);
    }

    private Users getUserById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id));
    }
}

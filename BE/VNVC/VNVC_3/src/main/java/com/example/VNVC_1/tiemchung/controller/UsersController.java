package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.service.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService userService) {
        this.usersService = userService;
    }

    @GetMapping
    @Tag(name ="Lấy danh sách toàn bộ user")
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Tag(name ="Lấy user bằng id" )
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @PostMapping("/new-user")
    @Tag(name ="Tạo user")
    public ResponseEntity<Users> createUser(@RequestBody Users users) {
        return ResponseEntity.ok(usersService.createUser(users));
    }

    @PutMapping("/{id}")
    @Tag(name ="Admin, Staff cập nhật user")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        return ResponseEntity.ok(usersService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    @Tag(name ="Admin, Staff xóa user")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    // API 1: Lấy hồ sơ trẻ em
    @GetMapping("/{id}/child-profile")
    @Tag(name ="Lấy hồ sơ con của user")
    public ResponseEntity<List<Users>> getChildProfiles(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getChildProfiles(id));
    }

    // API 2: Lấy phản ứng sau tiêm
    @GetMapping("/{id}/post-vaccination-reactions")
    @Tag(name ="Xem phản ứng sau tiêm")
    public ResponseEntity<Object> getPostVaccinationReactions(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getPostVaccinationReactions(id));
    }

    // API 3: Lấy lịch tiêm chủng

    @GetMapping("/{id}/vaccination-schedule")
    @Tag(name ="Xem lịch tiêm chủng")
    public ResponseEntity<Object> getVaccinationSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getVaccinationSchedule(id));
    }

    // API 4: Lấy lịch sử tiêm chủng
    @GetMapping("/{id}/vaccination-history")
    @Tag(name ="Xem lịch sử tiêm chủng")
    public ResponseEntity<Object> getVaccinationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getVaccinationHistory(id));
    }

    // API 5: Lấy thông tin của cha mẹ
    @GetMapping("/{id}/parent-info")
    @Tag(name ="Xem thông tin user (cha mẹ)")
    public ResponseEntity<Users> getParentInfo(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getParentInfo(id));
    }

    // API 6: Cập nhật hồ sơ trẻ em
    @PutMapping("/{id}/update-child-profile")
    @Tag(name ="Cập nhật hồ sơ child")
    public ResponseEntity<Users> updateChildProfile(@PathVariable Long id, @RequestBody Users childDetails) {
        return ResponseEntity.ok(usersService.updateChildProfile(id, childDetails));
    }

    // API 7: Cập nhật hồ sơ cha mẹ
    @PutMapping("/{id}/update-parent-profile")
    @Tag(name ="Cập nhật hồ sơ user (cha mẹ)")
    public ResponseEntity<Users> updateParentProfile(@PathVariable Long id, @RequestBody Users parentDetails) {
        return ResponseEntity.ok(usersService.updateParentProfile(id, parentDetails));
    }

    // API 8: Cập nhật lịch booking
    @PutMapping("/{id}/update-booking")
    @Tag(name ="Cập nhật lịch booking")
    public ResponseEntity<Object> updateBooking(@PathVariable Long id, @RequestBody Object bookingDetails) {
        return ResponseEntity.ok(usersService.updateBooking(id, bookingDetails));
    }

    // API 9: Xóa lịch booking
    @DeleteMapping("/{id}/delete-booking")
    @Tag(name ="Xóa lịch booking")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        usersService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully.");
    }


}

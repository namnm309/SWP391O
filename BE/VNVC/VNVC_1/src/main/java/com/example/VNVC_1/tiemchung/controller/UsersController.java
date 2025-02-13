package com.example.VNVC_1.tiemchung.controller;

import com.example.VNVC_1.tiemchung.model.Users;
import com.example.VNVC_1.tiemchung.service.UsersService;
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
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users users) {
        return ResponseEntity.ok(usersService.createUser(users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        return ResponseEntity.ok(usersService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    // API 1: Lấy hồ sơ trẻ em
    @GetMapping("/{id}/child-profile")
    public ResponseEntity<List<Users>> getChildProfiles(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getChildProfiles(id));
    }

    // API 2: Lấy phản ứng sau tiêm
    @GetMapping("/{id}/post-vaccination-reactions")
    public ResponseEntity<Object> getPostVaccinationReactions(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getPostVaccinationReactions(id));
    }

    // API 3: Lấy lịch tiêm chủng
    @GetMapping("/{id}/vaccination-schedule")
    public ResponseEntity<Object> getVaccinationSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getVaccinationSchedule(id));
    }

    // API 4: Lấy lịch sử tiêm chủng
    @GetMapping("/{id}/vaccination-history")
    public ResponseEntity<Object> getVaccinationHistory(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getVaccinationHistory(id));
    }

    // API 5: Lấy thông tin của cha mẹ
    @GetMapping("/{id}/parent-info")
    public ResponseEntity<Users> getParentInfo(@PathVariable Long id) {
        return ResponseEntity.ok(usersService.getParentInfo(id));
    }

    // API 6: Cập nhật hồ sơ trẻ em
    @PutMapping("/{id}/update-child-profile")
    public ResponseEntity<Users> updateChildProfile(@PathVariable Long id, @RequestBody Users childDetails) {
        return ResponseEntity.ok(usersService.updateChildProfile(id, childDetails));
    }

    // API 7: Cập nhật hồ sơ cha mẹ
    @PutMapping("/{id}/update-parent-profile")
    public ResponseEntity<Users> updateParentProfile(@PathVariable Long id, @RequestBody Users parentDetails) {
        return ResponseEntity.ok(usersService.updateParentProfile(id, parentDetails));
    }

    // API 8: Cập nhật lịch booking
    @PutMapping("/{id}/update-booking")
    public ResponseEntity<Object> updateBooking(@PathVariable Long id, @RequestBody Object bookingDetails) {
        return ResponseEntity.ok(usersService.updateBooking(id, bookingDetails));
    }

    // API 9: Xóa lịch booking
    @DeleteMapping("/{id}/delete-booking")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        usersService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully.");
    }


}

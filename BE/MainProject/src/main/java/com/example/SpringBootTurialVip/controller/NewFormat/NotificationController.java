package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.entity.Notification;
import com.example.SpringBootTurialVip.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name="[Notification]",description = "")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "API gửi thông báo đến tất cả staff(admin)", description = "Cho phép admin gửi thông báo đến tất cả nhân viên có role STAFF.")
    @PostMapping("/notifications/staff")
    public ResponseEntity<String> sendNotificationToAllStaff(@RequestParam String message) {
        notificationService.sendNotificationToAllStaff(message);
        return ResponseEntity.ok("Notification sent to all staff successfully");
    }

//    @Operation(summary = "Gửi thông báo đến toàn bộ nhân viên", description = "Gửi một thông báo đến tất cả nhân viên trong hệ thống")
//    @PostMapping("/send-notification")
//    public ResponseEntity<ApiResponse<String>> sendNotification(@RequestParam String message) {
//        adminDashboardService.sendNotificationToStaff(message);
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Notification sent successfully", null));
//    }

    @PreAuthorize("hasAnyRole('STAFF')")
    @Operation(summary = "API gửi thông báo đến khách hàng(staff)",
            description = "Staff có thể gửi thông báo đến khách hàng.")
    @PostMapping("/notifications")
    public ResponseEntity<Notification> sendNotification(
            @RequestParam Long userId,
            @RequestParam String message) {
        return ResponseEntity.ok(notificationService.sendOrderStatusNotification(userId, message));
    }

    //API xem thông báo
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @Operation(summary = "API xem danh sách thông báo cùa mình (customer,staff)", description = "Trả về danh sách thông báo của account .")
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    //API đánh dấu thông báo đã đọc
    @PreAuthorize("hasAnyRole('STAFF','CUSTOMER')")
    @Operation(summary = "API đánh dấu thông báo đã đọc(customer,staff)", description = "Cho phép khách hàng đánh dấu thông báo là đã đọc.")
    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    



}

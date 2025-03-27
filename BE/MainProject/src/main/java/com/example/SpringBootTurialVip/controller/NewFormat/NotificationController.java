package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.NotificationResponseDTO;
import com.example.SpringBootTurialVip.dto.response.NotificationSentDTO;
import com.example.SpringBootTurialVip.entity.Notification;
import com.example.SpringBootTurialVip.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name="[Notification]",description = "")
@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "API gửi thông báo đến tất cả staff(admin)",
            description = "Cho phép admin gửi thông báo đến tất cả nhân viên có role STAFF.")
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

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','ROLE_ROLE_STAFF')")
    @Operation(summary = "API gửi thông báo đến khách hàng chỉ định (staff)",
            description = "Staff có thể gửi thông báo đến khách hàng.")
    @PostMapping("/notifications")
    public ResponseEntity<Notification> sendNotification(
            @RequestParam Long userId,
            @RequestParam String message) {
        return ResponseEntity.ok(notificationService.sendNotification(userId, message));
    }

    //API xem thông báo
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN','ROLE_ROLE_STAFF')")
    @Operation(summary = "API xem danh sách thông báo của mình (customer,staff)", description = "Trả về danh sách thông báo nhận được.")
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        List<Notification> notifications = notificationService.getUserNotifications(userId);
        List<NotificationResponseDTO> result = notifications.stream()
                .map(notificationService::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }


    //API đánh dấu thông báo đã đọc
    @PreAuthorize("hasAnyRole('STAFF','CUSTOMER','ADMIN')")
    @Operation(summary = "API đánh dấu thông báo đã đọc(customer,staff)",
            description = "Cho phép khách hàng đánh dấu thông báo là đã đọc.")
    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    @PreAuthorize("hasAnyRole('STAFF','CUSTOMER','ROLE_ROLE_STAFF','ADMIN')")
    @Operation(summary = "API đánh dấu tất cả thông báo đã đọc (customer, staff)",
            description = "Cho phép khách hàng và nhân viên đánh dấu tất cả thông báo là đã đọc.")
    @PutMapping("/notifications/read-all")
    public ResponseEntity<String> markAllNotificationsAsRead() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read");
    }


    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API gửi thông báo đến tất cả khách hàng (staff,admin)",
            description = "Staff có thể gửi thông báo đến tất cả khách hàng chỉ bằng cách nhập nội dung tin nhắn.")
    @PostMapping("/all")
    public ResponseEntity<ApiResponse<String>> sendNotificationToAllCustomers(
            @Parameter(description = "Nội dung thông báo cần gửi", required = true, example = "Hệ thống bảo trì vào 10h tối nay.")
            @RequestParam String message) {

        notificationService.sendNotificationToAllCustomers(message);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Thông báo đã được gửi đến tất cả khách hàng!", "Success"));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Xem các thông báo mình đã gửi", description = "Lấy danh sách notification có mình là người gửi")
    @GetMapping("/notifications/sent")
    public ResponseEntity<List<NotificationSentDTO>> getSentNotifications() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(notificationService.getNotificationsSentByPublic(userId));
    }


}







package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ConsultRequestDTO;
import com.example.SpringBootTurialVip.dto.request.StaffProcessDTO;
import com.example.SpringBootTurialVip.entity.ConsultationRequest;
import com.example.SpringBootTurialVip.enums.RequestStatus;
import com.example.SpringBootTurialVip.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="[Form]",description = "")
@RestController
@RequestMapping("/consult")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    /**
     * API để nhận yêu cầu tư vấn từ khách hàng.
     */
    @Operation(summary = "Nhận yêu cầu tư vấn từ khách hàng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Yêu cầu tư vấn thành công"),
            @ApiResponse(responseCode = "400", description = "Lỗi khi gửi yêu cầu tư vấn")
    })
    @PostMapping("/request")
    public ResponseEntity<String> createConsultationRequest(@RequestBody ConsultRequestDTO consultRequestDTO) {
        try {
            consultationService.createConsultationRequest(consultRequestDTO);
            return ResponseEntity.ok("Yêu cầu tư vấn đã được gửi thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lỗi khi gửi yêu cầu tư vấn: " + e.getMessage());
        }
    }

    /**
     * API để staff xử lý yêu cầu tư vấn.
     */
//    @Operation(summary = "Xử lý yêu cầu tư vấn từ staff")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Yêu cầu tư vấn đã được xử lý"),
//            @ApiResponse(responseCode = "400", description = "Lỗi khi xử lý yêu cầu tư vấn")
//    })
//    @PostMapping("/staff/process")
//    public ResponseEntity<String> processConsultationRequest(@RequestBody StaffProcessDTO staffProcessDTO) {
//        try {
//            consultationService.processConsultationRequest(staffProcessDTO);
//            return ResponseEntity.ok("Yêu cầu tư vấn đã được xử lý thành công");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Lỗi khi xử lý yêu cầu tư vấn: " + e.getMessage());
//        }
//    }

    /**
     * API để lấy danh sách yêu cầu tư vấn.
     */
    @Operation(summary = "Lấy danh sách yêu cầu tư vấn")
    @ApiResponse(responseCode = "200", description = "Danh sách yêu cầu tư vấn")
    @GetMapping
    public ResponseEntity<List<ConsultationRequest>> getAllConsultationRequests() {
        return ResponseEntity.ok(consultationService.getAllConsultationRequests());
    }

    /**
     * API để cập nhật trạng thái yêu cầu tư vấn.
     */
    @Operation(summary = "Cập nhật trạng thái yêu cầu tư vấn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trạng thái yêu cầu đã được cập nhật"),
            @ApiResponse(responseCode = "400", description = "Lỗi khi cập nhật trạng thái")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRequestStatus(@PathVariable Long id, @RequestParam RequestStatus status) {
        consultationService.updateRequestStatus(id, status);
        return ResponseEntity.ok("Trạng thái yêu cầu đã được cập nhật.");
    }
}

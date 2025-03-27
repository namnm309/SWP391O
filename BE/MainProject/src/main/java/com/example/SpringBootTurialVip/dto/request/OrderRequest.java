package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(description = "Yêu cầu tạo đơn hàng tiêm vaccine")
public class OrderRequest {

	@Schema(description = "Họ của người đặt", example = "Nguyễn Văn")
	private String firstName;

	@Schema(description = "Tên của người đặt", example = "An")
	private String lastName;

	@Schema(description = "Email liên hệ", example = "example@gmail.com")
	private String email;

	@Schema(description = "Số điện thoại liên hệ", example = "0123456789")
	private String mobileNo;

	@Schema(
			description = "Map giữa childId (ID của trẻ) và danh sách productId (ID vaccine). Mỗi trẻ có thể chọn nhiều vaccine.",
			example = "{ \"28\": [50, 23, 17], \"53\": [13] }"
	)
	private Map<Long, List<Long>> childProductMap;

	@Schema(
			description = "Ngày tiêm mong muốn cho mũi đầu tiên (toàn bộ vaccine). Chỉ chấp nhận giờ trong khoảng 07:30 - 17:00.",
			example = "2025-03-28T08:30:00"
	)
	private LocalDateTime vaccinationdate;


}


package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateProfileRequest {

    @Schema(example = "Nguyen Van A", description = "Họ và tên của người dùng")
    private String fullname;

    @Schema(example = "0123456789", description = "Số điện thoại của người dùng")
    private String phone;

    @Schema(example = "1999-01-01", description = "Ngày sinh của người dùng (YYYY-MM-DD)")
    private Date bod;

    @Schema(example = "Male", description = "Giới tính của người dùng (Male/Female)")
    private String gender;

    @Schema(example = "newSecurePassword123", description = "Mật khẩu mới (nếu cần cập nhật)")
    private String password;


}

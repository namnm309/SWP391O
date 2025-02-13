package com.example.SpringBootTurialVip.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    //Lỗi trùng username
    USER_EXISTED(1001,"User đã tồn tại"),//Define 1 mã errorcode,
    //Lỗi ko xác định
    UNCATEGORIZED_EXCEPTION(9999,"Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    //Lỗi input phải lớn hơn 8
    PASSWORD_INVALID(1003,"Mật khẩu phải ít nhất 8 kí tự "),
    //Lỗi nay do chưa gán thông báo lỗi cho 1 service , do backend
    INVALIDID_KEY(1002,"Lỗi sai mã định danh lỗi . Hãy lưu lại và báo với BackEnd , Xin cảm ơn ."),
    //Lỗi ko tìm thấy username
    USER_NOT_EXISTED(1006,"Tài khoản không tồn tại . Xin vui lòng thử lại !!!"),
    //Lỗi ko xác thực
    UNAUTHENTICATED(1007,"Tài khoản ko hợp lệ !")
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode=statusCode;
    }


}

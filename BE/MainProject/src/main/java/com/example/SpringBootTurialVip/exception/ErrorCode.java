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
    UNAUTHENTICATED(1007,"Tài khoản ko hợp lệ !"),
    //Lỗi tài khoản chưa kích hoạt
    USER_DISABLED(1008, "Tài khoản của bạn chưa được kích hoạt hoặc đã bị vô hiệu hóa."),
    //Lỗi trẻ đã tồn tại
    CHILD_EXISTED(1009,"Trẻ đã tồn tại !"),
    //Lỗi trẻ ko tồn tại
    CHILD_NOT_EXISTED(1010,"Trẻ không tồn tại!"),
    //Lỗi ko xác định moii61 quan hệ khi tạo trẻ
    INVALID_RELATIONSHIP_TYPE(1011,"Khong6 xác định được quan hệ với trẻ !!!"),
    //Lỗi ko tìm thấy trẻ
    CHILD_NOT_FOUND(10012,"Không tìm thấy trẻ trong hệ thống , vui lòng nhập lại !"),
    //Lỗi xác thực trẻ có phải con của user đang log in hay ko
    UNAUTHORIZED_ACTION(10013,"Bạn không có quyền truy cập vào trẻ này !"),

    FILE_UPLOAD_FAILED(10014,"Lỗi ko upload đc ảnh"),

    EMAIL_ALREADY_EXISTS(10015,"Email đã tồn tại "),

    PHONE_ALREADY_EXISTS(10016,"Số đt đã tòn tại ")


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

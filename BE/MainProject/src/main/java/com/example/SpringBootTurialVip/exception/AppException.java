package com.example.SpringBootTurialVip.exception;

//Thay vì phải RunTimeException tại mỗi API thì tạo quy chuẩn chung = claass
//Và để sử dụng class này phải define trong GlobalException
public class AppException extends  RuntimeException{

    private ErrorCode errorCode;


    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());//Kế thừa constructor của RuntimeException
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}

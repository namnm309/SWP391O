package com.example.SpringBootTurialVip.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)//Field nào null sẽ ko hiển thị lên API Respone, nhưng ko nên xài
//Form response API
//Class này chứa các field mong muốn để chuẩn hóa API
@Data
@Builder
//@SuperBuilder //sp kiểu generic
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)

public class ApiResponse <T> {


    private int code=1000;//user có thể tra đc code này báo tình trạng gì,1000 là thành công
    private String message;
    private T result;//T là 1 kiểu dữ liệu generic có thể thay đổi theo từng loại API khác nhau

//    public ApiResponse() {
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public T getResult() {
//        return result;
//    }
//
//    public void setResult(T result) {
//        this.result = result;
//    }
  //Constructor không tham số
//    public ApiResponse() {}
//
//    //  Constructor đầy đủ
//    public ApiResponse(int code, String message, T result) {
//        this.code = code;
//        this.message = message;
//        this.result = result;
//    }
//
//    //  Getter và Setter
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public T getResult() {
//        return result;
//    }
//
//    public void setResult(T result) {
//        this.result = result;
//    }
//
//    //  Builder class thủ công
//    public static class Builder<T> {
//        private int code = 1000;
//        private String message;
//        private T result;
//
//        public Builder<T> code(int code) {
//            this.code = code;
//            return this;
//        }
//
//        public Builder<T> message(String message) {
//            this.message = message;
//            return this;
//        }
//
//        public Builder<T> result(T result) {
//            this.result = result;
//            return this;
//        }
//
//        public ApiResponse<T> build() {
//            return new ApiResponse<>(code, message, result);
//        }
//    }
//
//    //  Static method để tạo Builder
//    public static <T> Builder<T> builder() {
//        return new Builder<>();
//    }
}

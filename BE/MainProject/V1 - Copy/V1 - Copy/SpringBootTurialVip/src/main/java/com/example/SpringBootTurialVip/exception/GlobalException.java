package com.example.SpringBootTurialVip.exception;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice//Khai báo đây là nởi xử lí exception
public class GlobalException {

//    @ExceptionHandler(value= RuntimeException.class)
//    ResponseEntity<String> handlingRuntimeExcpetion(RuntimeException exception){//Spring sẽ auto inject exception vào exception
//        return ResponseEntity.badRequest()//lỗi 40x cho user
//                                .body(exception
//                                        .getMessage());
//    }
//
//    @ExceptionHandler(value= RuntimeException.class)
//    ResponseEntity<ApiResponse> handlingRuntimeExcpetion(RuntimeException exception){//Spring sẽ auto inject exception vào exception
//        ApiResponse apiResponse=new ApiResponse();
//
////        apiResponse.setCode(1001); này là đang hardcode giả định một mã error , trong realilty => different => ErrorCode(Enum) để define
//        apiResponse.setMessage(exception.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

//    @ExceptionHandler(value= Exception.class)
//    ResponseEntity<String> handlingRuntimeExcpetion(RuntimeException exception){//Spring sẽ auto inject exception vào exception
//        return ResponseEntity.badRequest()//lỗi 40x cho user
//                .body(exception
//                        .getMessage());
//    }

//Nếu chưa define lỗi trong errorcode method này sẽ nhảy ra thông báo lỗi ko xác định
//    @ExceptionHandler(value= Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeExcpetion(RuntimeException exception){//Spring sẽ auto inject exception vào exception
//        ApiResponse apiResponse=new ApiResponse();
//
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    //Xử lí lỗi trong quá trình truy vấn
    @ExceptionHandler(value= AppException.class)
    ResponseEntity<ApiResponse> handlingAppExcpetion(AppException exception){//Spring sẽ auto inject exception vào exception

        ErrorCode errorCode=exception.getErrorCode();


        ApiResponse apiResponse=new ApiResponse();

        apiResponse.setCode(errorCode.getCode());

        apiResponse.setMessage(exception.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    //Xử lí validation
//    @ExceptionHandler(value= MethodArgumentNotValidException.class)
//    ResponseEntity<String> handlingvalidation(MethodArgumentNotValidException exception){
//        return  ResponseEntity.badRequest()
//                .body(exception
//                        .getFieldError()//cho biết field nào sai do user nhập vào
//                        .getDefaultMessage());//lấy message đã triển khai defalut(do dev definition) khi xảy ra bug
//    }

    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingvalidation(MethodArgumentNotValidException exception){
        String enumKey=exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode=ErrorCode.INVALIDID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch ( IllegalArgumentException e) {
            //Chưa có log => skip
        }

        ApiResponse apiResponse=new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}

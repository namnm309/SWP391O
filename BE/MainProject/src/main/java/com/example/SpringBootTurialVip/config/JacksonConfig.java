//package com.example.SpringBootTurialVip.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//@Configuration
//public class JacksonConfig {
//
//    @BeanA
//    public ObjectMapper objectMapper() {
//        // Cấu hình ObjectMapper
//        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
//                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
//                .build();
//
//        // Nếu bạn muốn cấu hình độ sâu tối đa, có thể tham khảo cách khác (ví dụ: sử dụng các giới hạn trong controller)
//        // Cách này sẽ giúp bạn cấu hình ObjectMapper tùy chỉnh theo yêu cầu.
//
//        return objectMapper;
//    }
//}

package com.example.SpringBootTurialVip.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//Báo cho Spring boot biết đây là lớp controller để nó map các api phía dưới
@Tag(name="Tét")
public class BeginController {

    @GetMapping("/hello")
    String sayHello(){
        return "Khởi đầu Spring Boot API";
    }
}

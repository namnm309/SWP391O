package com.example.SpringBootTurialVip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController//Báo cho Spring boot biết đây là lớp controller để nó map các api phía dưới
public class BeginController {

    @GetMapping("/hello")
    String sayHello(){
        return "Khởi đầu Spring Boot API";
    }
}

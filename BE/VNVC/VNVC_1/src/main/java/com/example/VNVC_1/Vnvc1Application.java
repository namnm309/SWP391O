package com.example.VNVC_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.VNVC_1.tiemchung"})

public class Vnvc1Application {

	public static void main(String[] args) {
		SpringApplication.run(Vnvc1Application.class, args);
	}

}

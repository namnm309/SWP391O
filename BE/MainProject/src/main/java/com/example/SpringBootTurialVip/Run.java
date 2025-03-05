package com.example.SpringBootTurialVip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//@ComponentScan(basePackages = {"com.example.SpringBootTurialVip.controller.OldFormat.UserController"})
public class Run {
	public static void main(String[] args) {
		SpringApplication.run(Run.class, args);
	}
}



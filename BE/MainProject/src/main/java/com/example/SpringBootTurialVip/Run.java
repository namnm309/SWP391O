package com.example.SpringBootTurialVip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling  // Kích hoạt tính năng cron job

//@ComponentScan(basePackages = {"com.example.SpringBootTurialVip.controller.OldFormat.UserController"})
public class Run {

	public static void main(String[] args) {
		SpringApplication.run(Run.class, args);
	}

}



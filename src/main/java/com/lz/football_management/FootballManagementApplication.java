package com.lz.football_management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.lz.football_management.dao")
@SpringBootApplication
public class FootballManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballManagementApplication.class, args);
	}

}

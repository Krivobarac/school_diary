package com.iktpreobuka.schooldiary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iktpreobuka.schooldiary.services.RoleServiceImpl;

import ch.qos.logback.core.Context;

@SpringBootApplication
public class SchoolDiaryApplication {
	
	@Autowired
	RoleServiceImpl roleServ;

	@RequestMapping(value = "schoolDiary")
	public static void main(String[] args) {
		SpringApplication.run(SchoolDiaryApplication.class, args);
	}
}

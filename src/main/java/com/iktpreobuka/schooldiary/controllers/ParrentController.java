package com.iktpreobuka.schooldiary.controllers;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.controllers.utils.ErrorMessage;
import com.iktpreobuka.schooldiary.repositories.ParentRepository;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.RoleService;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping("/schoolDiary/users/admin")
public class ParrentController {
	
		@Autowired
		private ParentRepository parentRepository;
		@Autowired
		private AddressService addressServ;
		@Autowired
		private AccountService accountServ;
		@Autowired
		private RoleService roleServ;
		@Autowired
		private ErrorMessage errMsg;
}

package com.iktpreobuka.schooldiary.controllers;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.repositories.UserRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping("/schoolDiary/")
public class LoginController {
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "login")
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_PARRENT", "ROLE_TEACHER", "ROLE_STUDENT", "ROLE_DIRECTOR"})
	public ResponseEntity<?> getLogedUser(Principal principal) {
		try {
			UserEntity user = userRepository.findByAccountUserName(principal.getName());
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "forgotten")
	public ResponseEntity<?> getForgottenUser() {
		return new ResponseEntity<String>("Odobreno", HttpStatus.OK);
	}
}

package com.iktpreobuka.schooldiary.controllers;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rozidan.springboot.logger.Loggable;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping("/schoolDiary/users/u")
public class UserController {

}

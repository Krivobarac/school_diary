package com.iktpreobuka.schooldiary.controllers.utils;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
public class ErrorMessage {

	public String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
}

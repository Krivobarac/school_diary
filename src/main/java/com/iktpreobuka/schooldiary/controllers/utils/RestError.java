package com.iktpreobuka.schooldiary.controllers.utils;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

public class RestError {
	@JsonView(Views.User.class)
	private Integer code;
	@JsonView(Views.User.class)
	private String message;

	public RestError(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

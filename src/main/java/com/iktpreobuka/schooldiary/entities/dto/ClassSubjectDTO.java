package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

public class ClassSubjectDTO {
	public static Integer orderNumber = 0;
	@NotNull(message = "Razred je opavezan!")
	@JsonView(Views.Admin.class)
	@Pattern(regexp = "^[1-8]{1}$")
	private String schoolClass;
	@NotBlank(message = "Morate upisati nedeljni broj casova!")
	@JsonView(Views.Admin.class)
	private Integer fundWeaklyHours;
	public ClassSubjectDTO(String schoolClass, Integer fundWeaklyHours) {
		super();
		this.schoolClass = schoolClass;
		this.fundWeaklyHours = fundWeaklyHours;
	}
	
	public ClassSubjectDTO() {
		super();
	}

	public String getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(String schoolClass) {
		this.schoolClass = schoolClass;
	}

	public Integer getFundWeaklyHours() {
		return fundWeaklyHours;
	}

	public void setFundWeaklyHours(Integer fundWeaklyHours) {
		this.fundWeaklyHours = fundWeaklyHours;
	}
	
}

package com.iktpreobuka.schooldiary.entities.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

public class SubjectDTO {
	
	public static Integer orderNumber = 0;
	@JsonView(Views.User.class)
	@NotBlank(message = "Predmet je obavezan!")
	private String subjectName;
	@JsonView(Views.Admin.class)
	@NotNull(message = "Obavezno polje!")
	private Boolean optional = false;
	@JsonView(Views.SuperAdmin.class)
	@NotNull(message = "Morate uneti odgovarajuce razrede i fond casova!")
	private List<ClassSubjectDTO> classList = new ArrayList<>();
	
	public SubjectDTO(String className, Boolean optional, List<ClassSubjectDTO> classList) {
		super();
		this.subjectName = className;
		this.optional = optional;
		this.classList = classList;
	}

	public SubjectDTO() {
		super();
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String className) {
		this.subjectName = className;
	}

	public Boolean getOptional() {
		return optional;
	}

	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	public List<ClassSubjectDTO> getClassList() {
		return classList;
	}

	public void setClassList(List<ClassSubjectDTO> classList) {
		this.classList = classList;
	}
	
}

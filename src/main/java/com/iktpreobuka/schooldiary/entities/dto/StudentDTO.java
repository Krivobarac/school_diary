package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class StudentDTO extends UserDTO{
	@NotNull(message = "Skolska godina je obavezna!")
	@Pattern(regexp = "^[0-9]{4}/[0-9]{4}$",  message = "Skolsku godinu unesite u  formatu yyyy/yyyy!")
	private String schoolYear;
	@NotNull(message = "Broj skole je obavezan!")
	@Min(value = 999999999L, message = "Broj skole mora imati minimum 10 cifara!")
	private Long schoolNumber;
	@NotNull(message = "Razred je obavezan!")
	@Min(value = 1, message = "Razred se mora upisati kao broj od 1 do 8!")
	@Max(value = 8, message = "Razred se mora upisati kao broj od 1 do 8!")
	private Integer grade;
	
	public StudentDTO(String schoolYear, Long schoolNumber, Integer grade) {
	super();
	this.schoolYear = schoolYear;
	this.schoolNumber = schoolNumber;
	this.grade = grade;
	}

	public StudentDTO() {
		super();
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Long getSchoolNumber() {
		return schoolNumber;
	}

	public void setSchoolNumber(Long schoolNumber) {
		this.schoolNumber = schoolNumber;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
}

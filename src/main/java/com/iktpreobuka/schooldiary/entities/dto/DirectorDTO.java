package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DirectorDTO extends UserDTO {
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	private String email;
	@NotNull(message = "Broj skole je obavezan!")
	@Min(value = 9999999999L, message = "Broj skole mora imati minimum 11 cifara!")
	private Long schoolNumber;

	public DirectorDTO(String email, Long schoolNumber) {
		super();
		this.email = email;
		this.schoolNumber = schoolNumber;
	}

	public DirectorDTO() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getSchoolNumber() {
		return schoolNumber;
	}

	public void setSchoolNumber(Long schoolNumber) {
		this.schoolNumber = schoolNumber;
	}
	
}

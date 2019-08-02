package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SchoolDTO extends AddressDTO {
	
	@NotBlank(message = "Naziv skole je obavezan!")
	@NotEmpty(message = "Naziv skole je obavezan!")
	@NotNull(message = "Naziv skole je obavezan!")
	@Size(min = 5, max = 48, message = "Naziv skole mora imati izmedju 5 i 48 karaktera!")
	private String nameSchool;
	
	
	public SchoolDTO() {
		super();
	}

	public String getNameSchool() {
		return nameSchool;
	}

	public void setNameSchool(String nameSchool) {
		this.nameSchool = nameSchool;
	}
	
}

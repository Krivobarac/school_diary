package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO extends AddressDTO {
	@NotNull(message = "Ime je obavezno!")
	@Size(min = 2, max = 24, message = "Ime mora sadrzati od {min} do {max} karaktera!")
	protected String firstName;
	@NotNull(message = "Prezime je obavezno!")
	@Size(min = 2, max = 24, message = "Prezime mora sadrzati od {min} do {max} karaktera!")
	protected String lastName;
	@NotNull(message = "JMBG je obavezan!")
	@Pattern(regexp = "^[0-9]{13}$",  message = "JMBG mora imati 13 cifara!")
	protected String jmbg;
	@NotNull(message = "Pol je obavezan!")
	@Pattern(regexp = "(^Muski$)|(^Zenski$)", message = "Pol moze biti samo Muski ili Zenski. Nismo evropski orijentisani :D")
	protected String gender;
	
	public UserDTO() {
		super();
	}
	
	public UserDTO(String firstName, String lastName, String jmbg, String gender) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jmbg = jmbg;
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}

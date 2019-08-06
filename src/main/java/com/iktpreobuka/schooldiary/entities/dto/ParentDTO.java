package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ParentDTO {
	@NotNull(message = "Ime roditelja je obavezno!")
	@Size(min = 2, max = 24, message = "Ime mora sadrzati od {min} do {max} karaktera!")
	private String parentFirstName;
	@NotNull(message = "Prezime roditelja je obavezno!")
	@Size(min = 2, max = 24, message = "Prezime mora sadrzati od {min} do {max} karaktera!")
	private String parentLastName;
	@NotNull(message = "JMBG roditelja je obavezan!")
	@Pattern(regexp = "^[0-9]{13}$",  message = "JMBG mora imati 13 cifara!")
	private String parentJmbg;
	@NotNull(message = "Pol roditelja je obavezan!")
	@Pattern(regexp = "(^Muski$)|(^Zenski$)", message = "Pol moze biti samo Muski ili Zenski. Nismo evropski orijentisani :D")
	private String parentGender;
	@NotNull(message = "Email nije ispravan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	private String parentEmail;
	@NotBlank(message = "Ulica roditelja je obavezna!")
	@Size(min = 2, max = 20, message = "Naziv Ulice mora biti od {min} do {max} karaktera!")
	private String parentNameStreet;
	@NotBlank(message = "Stambeni broj roditelja je obavezan!")
	private String parentHouseNumber;
	@NotBlank(message = "Naziv grada roditelja je obavezan!")
	@Size(min = 2, max = 20, message = "Naziv grada mora biti od {min} do {max} karaktera!")
	private String parentNameCity;
	@NotBlank(message = "Naziv opstine roditelja je obavezan!")
	@Size(min = 2, max = 20, message = "Naziv opstine mora biti od {min} do {max} karaktera!")
	private String parentNameBorough;
	@NotNull(message = "Broj opstine roditelja je obavezan!")
	@Pattern(regexp = "^[0-9]{5,6}$", message = "Broj opstine mora imati 5 ili 6 cifara")
	private String parentNumberBorough;
	
	public ParentDTO(String parentFirstName, String parentLastName, String parentJmbg, String parentGender,
			String parentEmail, String parentNameStreet, String parentHouseNumber, String parentNameCity,
			String parentNameBorough, String parentNumberBorough) {
		super();
		this.parentFirstName = parentFirstName;
		this.parentLastName = parentLastName;
		this.parentJmbg = parentJmbg;
		this.parentGender = parentGender;
		this.parentEmail = parentEmail;
		this.parentNameStreet = parentNameStreet;
		this.parentHouseNumber = parentHouseNumber;
		this.parentNameCity = parentNameCity;
		this.parentNameBorough = parentNameBorough;
		this.parentNumberBorough = parentNumberBorough;
	}

	public ParentDTO() {
		super();
	}

	public String getParentFirstName() {
		return parentFirstName;
	}

	public void setParentFirstName(String parentFirstName) {
		this.parentFirstName = parentFirstName;
	}

	public String getParentLastName() {
		return parentLastName;
	}

	public void setParentLastName(String parentLastName) {
		this.parentLastName = parentLastName;
	}

	public String getParentJmbg() {
		return parentJmbg;
	}

	public void setParentJmbg(String parentJmbg) {
		this.parentJmbg = parentJmbg;
	}

	public String getParentGender() {
		return parentGender;
	}

	public void setParentGender(String parentGender) {
		this.parentGender = parentGender;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}

	public String getParentNameStreet() {
		return parentNameStreet;
	}

	public void setParentNameStreet(String parentNameStreet) {
		this.parentNameStreet = parentNameStreet;
	}

	public String getParentHouseNumber() {
		return parentHouseNumber;
	}

	public void setParentHouseNumber(String parentHouseNumber) {
		this.parentHouseNumber = parentHouseNumber;
	}

	public String getParentNameCity() {
		return parentNameCity;
	}

	public void setParentNameCity(String parentNameCity) {
		this.parentNameCity = parentNameCity;
	}

	public String getParentNameBorough() {
		return parentNameBorough;
	}

	public void setParentNameBorough(String parentNameBorough) {
		this.parentNameBorough = parentNameBorough;
	}

	public String getParentNumberBorough() {
		return parentNumberBorough;
	}

	public void setParentNumberBorough(String parentNumberBorough) {
		this.parentNumberBorough = parentNumberBorough;
	}
	
}

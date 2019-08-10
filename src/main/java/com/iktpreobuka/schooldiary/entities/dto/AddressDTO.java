package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AddressDTO {
	@NotBlank(message = "Ulica je obavezna!")
	@Size(min = 2, max = 20, message = "Naziv Ulice mora biti od {min} do {max} karaktera!")
	protected String nameStreet;
	@NotBlank(message = "Stambeni broj je obavezan!")
	protected String houseNumber;
	@NotBlank(message = "Naziv grada je obavezan!")
	@Size(min = 2, max = 20, message = "Naziv grada mora biti od {min} do {max} karaktera!")
	protected String nameCity;
	@NotBlank(message = "Naziv opstine je obavezan!")
	@Size(min = 2, max = 20, message = "Naziv opstine mora biti od {min} do {max} karaktera!")
	protected String nameBorough;
	@NotNull(message = "Broj opstine je obavezan!")
	@Pattern(regexp = "^[0-9]{5,6}$", message = "Broj opstine mora imati 5 ili 6 cifara")
	protected String numberBorough;
	
	public AddressDTO() {}
	
	public AddressDTO(String nameStreet, String houseNumber, String nameCity, String nameBorough, String numberBorough) {
		super();
		this.nameStreet = nameStreet;
		this.houseNumber = houseNumber;
		this.nameCity = nameCity;
		this.nameBorough = nameBorough;
		this.numberBorough = numberBorough;
	}


	public String getNameStreet() {
		return nameStreet;
	}

	public void setNameStreet(String nameStreet) {
		this.nameStreet = nameStreet;
	}

	public String getNameCity() {
		return nameCity;
	}

	public void setNameCity(String nameCity) {
		this.nameCity = nameCity;
	}

	public String getNameBorough() {
		return nameBorough;
	}

	public void setNameBorough(String nameBorough) {
		this.nameBorough = nameBorough;
	}

	public String getNumberBorough() {
		return numberBorough;
	}

	public void setNumberBorough(String numberBorough) {
		this.numberBorough = numberBorough;
	}
	
	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
}

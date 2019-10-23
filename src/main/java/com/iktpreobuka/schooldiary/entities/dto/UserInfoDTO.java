package com.iktpreobuka.schooldiary.entities.dto;

public class UserInfoDTO {
	private String schoolNumber = null;
	private String firstName = null;
	private String lastName = null;
	private String email = null;
	private String jmbg = null;
	private String gender = null;
	private String address = null;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSchoolNumber() {
		return schoolNumber;
	}
	public void setSchoolNumber(String schoolNumber) {
		this.schoolNumber = schoolNumber;
	}
}

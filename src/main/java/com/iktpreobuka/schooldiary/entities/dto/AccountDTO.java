package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AccountDTO {
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	private String email;
	@NotNull(message = "Korisnicko ime je obavezno!")
	@Size(min = 6, message = "Korisnicko ime mora imati minimim {min} karaktera!")
	private String userName;
	@NotNull(message = "Sifra je obavezna!")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$&*]).{6,}$", message = "Sira mora sadrzati minimum 6 karaktera (Jedno malo slovo, jedno veliko slovo, jedan broj i jedan specijalni karakter)")
	private String password;
	
	public AccountDTO() {
		super();
	}
	
	public AccountDTO(String userName, String password, String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

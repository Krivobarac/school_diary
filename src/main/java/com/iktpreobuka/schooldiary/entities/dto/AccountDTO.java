package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AccountDTO{
	@NotBlank(message = "Korisnicko ime je obavezno!")
	@NotEmpty(message = "Korisnicko ime je obavezno!")
	@NotNull(message = "Korisnicko ime je obavezno!")
	@Size(min = 6, message = "Korisnicko ime mora imati minimim {min} karaktera!")
	private String userName;
	@NotBlank(message = "Sifra je obavezna!")
	@NotEmpty(message = "Sifra je obavezna!")
	@NotNull(message = "Sifra je obavezna!")
	@Size(min = 6, message = "Sifra mora imati minimim {min} karaktera!")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$&*]).{6,}$", message = "Sira mora sadrzati minimum jedno malo slovo, jedno veliko slovo, jedan broj i jedan specijalni karakter")
	private String password;
	
	public AccountDTO() {
		super();
	}
	
	public AccountDTO(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
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
}

package com.iktpreobuka.schooldiary.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SuperAdminEntity extends UserEntity{
	@Column(nullable = false)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.User.class)
	private String email;

	public SuperAdminEntity(String firstName, String lastName, String jmbg, IGender gender, AccountEntity account, AddressEntity address, String email) {
		super(firstName, lastName, jmbg, gender, account, address);
		this.email = email;
	}
	
	public SuperAdminEntity(String email) {
		this.email = email;
	}

	public SuperAdminEntity() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}

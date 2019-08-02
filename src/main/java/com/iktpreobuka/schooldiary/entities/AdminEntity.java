package com.iktpreobuka.schooldiary.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonPropertyOrder({"email", "schoolUniqeNumber"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "schoolUniqeNumber"}))
public class AdminEntity extends UserEntity {
	@Column(nullable = false, unique = true)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.User.class)
	private String email;
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.Admin.class)
	private Integer schoolUniqeNumber;
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Views.User.class)
	@JsonManagedReference
	@JoinColumn(name = "idSchool", nullable = true)
	private SchoolEntity school;
	
	public AdminEntity() {
		super();
	}

	public SchoolEntity getSchools() {
		return school;
	}

	public void setSchools(SchoolEntity school) {
		this.school = school;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSchoolUniqeNumber() {
		return schoolUniqeNumber;
	}

	public void setSchoolUniqeNumber(Integer schoolUniqeNumber) {
		this.schoolUniqeNumber = schoolUniqeNumber;
	}

}

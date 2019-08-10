package com.iktpreobuka.schooldiary.entities;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolUniqeNumber"}))
public class AdminEntity extends UserEntity {
	@Column(nullable = false)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.Admin.class)
	private String email;
	@NotNull(message = "Broj skole je obavezan!")
	@Min(value = 999999999L, message = "Broj skole mora imati minimum 10 cifara!")
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.Admin.class)
	private Long schoolUniqeNumber;
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = {"school_id", "admin_id"}), name = "admin_school", 
	joinColumns = { @JoinColumn(name = "admin_id", referencedColumnName = "id_user", nullable = false) },
    inverseJoinColumns = { @JoinColumn(name = "school_id", referencedColumnName = "idSchool", nullable = false) })
	private SchoolEntity school;
	
	public AdminEntity(String firstName, String lastName, String jmbg, IGender gender, AccountEntity account, AddressEntity address, String email, Long schoolUniqeNumber, SchoolEntity school) {
		super(firstName, lastName, jmbg, gender, account, address);
		this.email = email;
		this.schoolUniqeNumber = schoolUniqeNumber;
		this.school = school;
	}

	public AdminEntity() {
		super();
	}

	public SchoolEntity getSchools() {
		return school;
	}

	public void setSchool(SchoolEntity school) {
		this.school = school;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getSchoolUniqeNumber() {
		return schoolUniqeNumber;
	}

	public void setSchoolUniqeNumber(Long schoolUniqeNumber) {
		this.schoolUniqeNumber = schoolUniqeNumber;
	}

}

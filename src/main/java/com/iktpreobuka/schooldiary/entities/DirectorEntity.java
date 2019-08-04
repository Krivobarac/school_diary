package com.iktpreobuka.schooldiary.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolUniqeNumber"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DirectorEntity extends UserEntity{
	@Column(nullable = false)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.User.class)
	private String email;
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.Admin.class)
	private Long schoolUniqeNumber;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "director_school", 
    joinColumns = { @JoinColumn(name = "director_id", referencedColumnName = "id_user") },
    inverseJoinColumns = { @JoinColumn(name = "school_id", referencedColumnName = "idSchool") })
	private SchoolEntity school;
	
	public DirectorEntity() {}
	

	public DirectorEntity(String firstName, String lastName, String jmbg, IGender gender, AccountEntity account, AddressEntity address, String email, Long schoolUniqeNumber, SchoolEntity school) {
		super(firstName, lastName, jmbg, gender, account, address);
		this.email = email;
		this.schoolUniqeNumber = schoolUniqeNumber;
		this.school = school;
	}


	public SchoolEntity getSchool() {
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

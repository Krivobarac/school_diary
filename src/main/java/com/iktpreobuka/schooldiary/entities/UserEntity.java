package com.iktpreobuka.schooldiary.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonPropertyOrder({"IdUser", "firstName", "lastName", "email", "schoolUniqeNumber", "grade", "jmbg", "gender", "createdAt", "deletedAt", "account", "address", "subject", "school", "schoolYear", "schoolClass", "teacher"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"IdUser"}))
public class UserEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.SuperAdmin.class)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	protected Integer IdUser;
	@NotBlank(message = "Ime ne sme biti prazno!")
	@NotNull(message = "Ime je obavezno!")
	@Size(min = 2, max = 24, message = "Ime mora sadrzati od {min} do {max} karaktera!")
	@Column(length = 24, nullable = false)
	@JsonView(Views.User.class)
	protected String firstName;
	@Size(min = 2, max = 24, message = "Prezime mora sadrzati od {min} do {max} karaktera!")
	@NotBlank(message = "Prezime je obavezno!")
	@NotNull(message = "Prezime je obavezno!")
	@Column(length = 24, nullable = false)
	@JsonView(Views.User.class)
	protected String lastName;
	@NotNull(message = "JMBG je obavezan!")
	@Pattern(regexp = "^[0-9]{13}$",  message = "JMBG mora imati 13 cifara!")
	@Column(nullable = false)
	@JsonView(Views.Admin.class)
	protected String jmbg;
	@NotNull(message = "Pol je obavezan!")
	@Column(nullable = false)
	@JsonView(Views.User.class)
	protected IGender gender;
	@Version
	@JsonView(Views.SuperAdmin.class)
	protected Integer version;
	@NotNull
	@JsonView(Views.Admin.class)
	@JsonFormat(pattern = "hh:MM:ss dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
	protected LocalDateTime createdAt = LocalDateTime.now();
	@JsonView(Views.Admin.class)
	@JsonFormat(pattern = "hh:MM:ss dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
	protected LocalDateTime deletedAt = null;
	@JsonView(Views.SuperAdmin.class)
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "id_account", unique = true)
	@JsonManagedReference
	protected AccountEntity account;
	@JsonView(Views.User.class)
	@JsonManagedReference
    @JoinColumn(name="id_address", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	protected AddressEntity address;

	public UserEntity() {}
	
	public UserEntity(String firstName, String lastName, String jmbg, IGender gender, AccountEntity account, AddressEntity address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jmbg = jmbg;
		this.gender = gender;
		this.account = account;
		this.address = address;
	}

	public Integer getIdUser() {
		return IdUser;
	}

	public void setIdUser(Integer idUser) {
		IdUser = idUser;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public IGender getGender() {
		return gender;
	}

	public void setGender(IGender gender) {
		this.gender = gender;
	}

	public AddressEntity getAddress() {
		return address;
	}

	public void setAddress(AddressEntity address) {
		this.address = address;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
	
}

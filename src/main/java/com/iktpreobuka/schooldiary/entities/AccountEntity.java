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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.securities.util.Encryption;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idAccount", "userName"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonIgnore
	private Integer idAccount;
	@Column(nullable = false, unique = true)
	@JsonView(Views.SuperAdmin.class)
	@NotBlank(message = "Korisnicko ime je obavezno!")
	@NotEmpty(message = "Korisnicko ime je obavezno!")
	@NotNull(message = "Korisnicko ime je obavezno!")
	@Size(min = 6, message = "Korisnicko ime mora imati minimim {min} karaktera!")
	private String userName;
	@JsonIgnore
	@Column(nullable = false)
	@NotBlank(message = "Sifra je obavezna!")
	@NotEmpty(message = "Sifra je obavezna!")
	@NotNull(message = "Sifra je obavezna!")
	@Size(min = 6, message = "Sifra mora imati minimim {min} karaktera!")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$&*]).{6,}$", message = "Sifra mora sadrzati minimum jedno malo slovo, jedno veliko slovo, jedan broj i jedan specijalni karakter")
	private String password;
	@JoinColumn(name = "id_role", nullable = false)
	@JsonView(Views.Admin.class)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonManagedReference
	@JsonIgnore
	private RoleEntity role;
	@Version
	@JsonIgnore
	private Integer version = null;
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonBackReference
	private List<UserEntity> users = new ArrayList<>();
	
	public AccountEntity() {}

	public AccountEntity(String userName, String password, RoleEntity role) {
		super();
		this.userName = userName;
		this.password = password;
		this.role = role;
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
		this.password = Encryption.getPassEncoded(password);
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(Integer idAccount) {
		this.idAccount = idAccount;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	
}

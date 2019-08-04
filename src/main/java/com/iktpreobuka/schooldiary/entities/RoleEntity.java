package com.iktpreobuka.schooldiary.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idRole", "role"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.SuperAdmin.class)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	private Integer idRole;
	@JsonView(Views.SuperAdmin.class)
	@Column(length = 24, nullable = false, unique = true, updatable = false)
	@NotNull
	@Enumerated(EnumType.STRING)
	private IRole role;
	@JsonBackReference
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<AccountEntity> accounts = new ArrayList<>();
	
	public RoleEntity() {}

	public RoleEntity(IRole role) {
		super();
		this.role = role;
	}

	public Integer getIdRole() {
		return idRole;
	}

	public void setIdRole(Integer idRole) {
		this.idRole = idRole;
	}

	public IRole getRole() {
		return role;
	}

	public void setRole(IRole role) {
		this.role = role;
	}

	public List<AccountEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountEntity> accounts) {
		this.accounts = accounts;
	}
}

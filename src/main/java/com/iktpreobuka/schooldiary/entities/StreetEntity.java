package com.iktpreobuka.schooldiary.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idStreet", "nameStreet"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StreetEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer idStreet;
	@Column(length = 24, nullable = false, unique = true)
	@JsonView(Views.User.class)
	@NotBlank(message = "Ulica je obavezna!")
	@Size(min = 2, max = 20, message = "Naziv Ulice mora biti od {min} do {max} karaktera!")
	private String nameStreet;
	@JsonIgnore
	@OneToMany(mappedBy = "street", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<AddressEntity> addresses = new ArrayList<>();
	@Version
	@JsonView(Views.SuperAdmin.class)
	private Integer version = null;
	
	public StreetEntity(String nameStreet) {
		this.nameStreet = nameStreet;
	}

	public StreetEntity() {
		super();
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getIdStreet() {
		return idStreet;
	}

	public void setIdStreet(Integer idStreet) {
		this.idStreet = idStreet;
	}

	public String getNameStreet() {
		return nameStreet;
	}

	public void setNameStreet(String nameStreet) {
		this.nameStreet = nameStreet;
	}

	public List<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}
	
}

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonSerialize
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idStreet", "nameStreet"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StreetEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonIgnore
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
	@JsonIgnore
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

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idNumber", "houseNumber"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HouseNumberEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonIgnore
	private Integer idNumber;
	@Column(length = 10, nullable = false, unique = true)
	@JsonView(Views.User.class)
	@NotBlank(message = "Stambeni broj je obavezan!")
	private String houseNumber;
	@Version
	@JsonIgnore
	private Integer version;
	@OneToMany(mappedBy = "houseNumber", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<AddressEntity> addresses = new ArrayList<>();
	
	public HouseNumberEntity() {
		super();
	}

	public HouseNumberEntity(String houseNumber) {
		super();
		this.houseNumber = houseNumber;
	}

	public Integer getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Integer idNumber) {
		this.idNumber = idNumber;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public List<AddressEntity> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}

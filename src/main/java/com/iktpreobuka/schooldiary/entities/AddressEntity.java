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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.ICountry;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonPropertyOrder({"idAddress", "id_city", "street", "houseNumber", "city"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_city", "id_street", "id_house_number"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AddressEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Views.SuperAdmin.class)
	@Column(length = 11, nullable = false, unique = true, updatable = false )
	private Integer idAddress;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_city", nullable = false)
	@JsonView(Views.User.class)
	@JsonManagedReference
	private CityEntity city;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_street", nullable = false)
	@JsonView(Views.User.class)
	@JsonManagedReference
	private StreetEntity street;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_house_number", nullable = false)
	@JsonView(Views.User.class)
	@JsonManagedReference
	private HouseNumberEntity houseNumber;
	@Transient
	@JsonView(Views.User.class)
	private ICountry country = ICountry.Srbija;
	@JsonView(Views.SuperAdmin.class)
	@Version
	private Integer version = null;
	@JsonIgnore
	@OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<UserEntity> users = new ArrayList<>();
	@JsonIgnore
	@OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<SchoolEntity> schools = new ArrayList<>();
	
	public AddressEntity(StreetEntity street, HouseNumberEntity houseNumber, CityEntity city) {
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
	}
	
	public AddressEntity() {
		super();
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	public StreetEntity getStreet() {
		return street;
	}

	public void setStreet(StreetEntity street) {
		this.street = street;
	}

	public HouseNumberEntity getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(HouseNumberEntity houseNumber) {
		this.houseNumber = houseNumber;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	public List<SchoolEntity> getSchools() {
		return schools;
	}

	public void setSchools(List<SchoolEntity> schools) {
		this.schools = schools;
	}

	public Integer getIdAddress() {
		return idAddress;
	}

	public void setIdAddress(Integer idAddress) {
		this.idAddress = idAddress;
	}

	public ICountry getCountry() {
		return country;
	}
}

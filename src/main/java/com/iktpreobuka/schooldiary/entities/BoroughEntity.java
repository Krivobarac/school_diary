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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.ICountry;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idBorough", "numberBorough"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BoroughEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer idBorough;
	@Column(length = 24, nullable = false)
	@JsonView(Views.User.class)
	@NotBlank(message = "Naziv opstine je obavezan!")
	@Size(min = 2, max = 20, message = "Naziv opstine mora biti od {min} do {max} karaktera!")
	private String nameBorough;
	@Column(length = 11, nullable = false, unique = true)
	@JsonView(Views.User.class)
	@NotNull(message = "Broj opstine je obavezan!")
	@Pattern(regexp = "^[0-9]{5,6}$", message = "Broj opstine mora imati 5 ili 6 cifara")
	private String numberBorough;
	@JsonView(Views.User.class)
	@Column
	@Enumerated(EnumType.ORDINAL)
	private ICountry country = ICountry.Srbija;
	@Column(length = 11)
	@JsonView(Views.SuperAdmin.class)
	private Integer version = null;
	@JsonBackReference
	@OneToMany(mappedBy = "borough", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<CityEntity> cities = new ArrayList<>();

	public BoroughEntity (String nameBorough, String numberBorough) {
		this.nameBorough = nameBorough;
		this.numberBorough = numberBorough;
	}

	public BoroughEntity() {}

	public Integer getIdBorough() {
		return idBorough;
	}

	public void setIdBorough(Integer idBorough) {
		this.idBorough = idBorough;
	}

	public String getNameBorough() {
		return nameBorough;
	}

	public void setNameBorough(String nameBorough) {
		this.nameBorough = nameBorough;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getNumberBorough() {
		return numberBorough;
	}
	
	public void setNumberBorough(String numberBorough) {
		this.numberBorough = numberBorough;
	}

	public List<CityEntity> getCities() {
		return cities;
	}

	public void setCities(List<CityEntity> cities) {
		this.cities = cities;
	}

	public ICountry getCountry() {
		return country;
	}

	public void setCountry(ICountry country) {
		this.country = country;
	}

}

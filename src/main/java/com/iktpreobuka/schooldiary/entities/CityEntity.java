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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idCity", "nameCity"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CityEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer idCity;
	@Column(length = 24, nullable = false, unique = true)
	@JsonView(Views.User.class)
	@NotBlank(message = "Naziv grada je obavezan!")
	@Size(min = 2, max = 20, message = "Naziv grada mora biti od {min} do {max} karaktera!")
	private String nameCity;
	@JoinColumn(name = "id_borough", nullable = false, referencedColumnName = "idBorough")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonView(Views.User.class)
	@JsonManagedReference
	private BoroughEntity borough;
	@Column(length = 11)
	@JsonView(Views.SuperAdmin.class)
	private Integer version = null;
	@JsonIgnore
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<AddressEntity> addresses = new ArrayList<>();
	
	public CityEntity() {
		super();
	}
	public CityEntity(String nameCity, BoroughEntity borough) {
		this.nameCity = nameCity;
		this.borough = borough;
	}


	public Integer getIdCity() {
		return idCity;
	}

	public void setIdCity(Integer idCity) {
		this.idCity = idCity;
	}

	public String getNameCity() {
		return nameCity;
	}

	public void setNameCity(String nameCity) {
		this.nameCity = nameCity;
	}

	public BoroughEntity getBorough() {
		return borough;
	}

	public void setBorough(BoroughEntity borough) {
		this.borough = borough;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	public List<AddressEntity> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressEntity> addresses) {
		this.addresses = addresses;
	}

}

package com.iktpreobuka.schooldiary.entities;

import java.time.LocalDateTime;
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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonPropertyOrder({"idSchool", "nameSchool", "numberSchool", "director", "admin", "createdAt", "address"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idSchool", "numberSchool"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SchoolEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer idSchool;
	@Column(length = 11, nullable = false, unique = true)
	@JsonView(Views.Teacher.class)
	private Long numberSchool;
	@Column(length = 48, nullable = false)
	@NotBlank(message = "Naziv skole je obavezan!")
	@NotEmpty(message = "Naziv skole je obavezan!")
	@NotNull(message = "Naziv skole je obavezan!")
	@Size(min = 5, max = 48, message = "Naziv skole mora imati izmedju 5 i 48 karaktera!")
	@JsonView(Views.User.class)
	private String nameSchool;
	@JoinColumn(name = "id_address", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonView(Views.User.class)
	@JsonManagedReference
	private AddressEntity address;
	@Version
	@JsonView(Views.SuperAdmin.class)
	private Integer version = null;
	@OneToOne(mappedBy = "school", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonView(Views.User.class)
	@JsonBackReference
	private DirectorEntity director;
	@OneToOne(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonView(Views.Teacher.class)
	@JsonBackReference
	private AdminEntity admin;
	@NotNull
	@JsonView(Views.SuperAdmin.class)
	@JsonFormat(pattern = "hh:MM:ss dd.MM.yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt = LocalDateTime.now();
	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "school_teacher", joinColumns = {@JoinColumn(name = "id_school", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_teacher", nullable = false)})
	private List<TeacherEntity> teachers = new ArrayList<>();
	@JsonIgnore
	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<StudentEntity> students = new ArrayList<>();
	@JsonIgnore
	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<ClassEntity> classes = new ArrayList<>();
	@JsonIgnore
	@OneToMany(mappedBy = "school", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<ClassEntity> users = new ArrayList<>();
	
	public SchoolEntity() {}

	public SchoolEntity(Long numberSchool, String nameSchool, AddressEntity address) {
		super();
		this.numberSchool = numberSchool;
		this.nameSchool = nameSchool;
		this.address = address;
	}

	public Integer getIdSchool() {
		return idSchool;
	}

	public void setIdSchool(Integer idSchool) {
		this.idSchool = idSchool;
	}

	public Long getNumberSchool() {
		return numberSchool;
	}

	public void setNumberSchool(Long numberSchool) {
		this.numberSchool = numberSchool;
	}

	public String getNameSchool() {
		return nameSchool;
	}

	public void setNameSchool(String nameSchool) {
		this.nameSchool = nameSchool;
	}

	public AddressEntity getAddress() {
		return address;
	}

	public void setAddress(AddressEntity address) {
		this.address = address;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<TeacherEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherEntity> teachers) {
		this.teachers = teachers;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}

	public DirectorEntity getDirector() {
		return director;
	}

	public void setDirector(DirectorEntity director) {
		this.director = director;
	}

	public AdminEntity getAdmin() {
		return admin;
	}

	public void setAdmin(AdminEntity admin) {
		this.admin = admin;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<ClassEntity> getUsers() {
		return users;
	}

	public void setUsers(List<ClassEntity> users) {
		this.users = users;
	}

}

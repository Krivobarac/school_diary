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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idSchoolYear", "schoolYear"}))
public class SchoolYearEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer idSchoolYear;
	@Column(length = 24, nullable = false, unique = true)
	@JsonView(Views.User.class)
	private String schoolYear;
	@JsonIgnore
	@OneToMany(mappedBy = "schoolYear", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<StudentEntity> students = new ArrayList<>();
	@OneToMany(mappedBy = "schoolYear", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonIgnore
	private List<ClassDepartmentEntity> classes = new ArrayList<>();
	@Version
	private Integer version = null;
	
	public SchoolYearEntity() {}

	public Integer getIdSchoolYear() {
		return idSchoolYear;
	}

	public void setIdSchoolYear(Integer idSchoolYear) {
		this.idSchoolYear = idSchoolYear;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public List<ClassDepartmentEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassDepartmentEntity> classes) {
		this.classes = classes;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}

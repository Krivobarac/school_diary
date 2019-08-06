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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idSubject", "subjectName"}))
public class SubjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer idSubject;
	@JsonView(Views.User.class)
	@Column(length = 100, nullable = false, unique = true)
	@NotBlank(message = "Predmet je obavezan!")
	private String subjectName;
	@JsonView(Views.Admin.class)
	@NotNull(message = "Obavezno polje!")
	@Column(nullable = false)
	private Boolean isActive = true;
	@JsonView(Views.Admin.class)
	@Column(nullable = false)
	private Boolean optional = false;
	@OneToMany(mappedBy = "subject")
	@JsonIgnore
	private List<TeacherEntity> teachers = new ArrayList<>();
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonIgnore
	private List<ClassSubjectEntity> classes = new ArrayList<>();
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonIgnore
	private List<EvaluationEntity> evaluations = new ArrayList<>();
	@Version
	private Integer version = null;
	
	public SubjectEntity() {}

	public SubjectEntity(String subjectName, Boolean optional) {
		super();
		this.subjectName = subjectName;
		this.optional = optional;
	}

	public Integer getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Integer idSubject) {
		this.idSubject = idSubject;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String name) {
		this.subjectName = name;
	}

	public List<TeacherEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherEntity> teachers) {
		this.teachers = teachers;
	}

	public List<ClassSubjectEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassSubjectEntity> classes) {
		this.classes = classes;
	}

	public List<EvaluationEntity> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<EvaluationEntity> evaluations) {
		this.evaluations = evaluations;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getOptional() {
		return optional;
	}

	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	
}

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idSubject", "name"}))
public class SubjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	private Integer idSubject;
	@Column(length = 24, nullable = false, unique = true)
	private String name;
	@Column(length = 5, nullable = false)
	private Integer fundWeaklyHours;
	@ManyToMany(mappedBy = "subjects")
	private List<TeacherEntity> teachers = new ArrayList<>();
	@ManyToMany(mappedBy = "subjects")
	private List<ClassEntity> classes = new ArrayList<>();
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<EvaluationEntity> evaluations = new ArrayList<>();
	@Version
	private Integer version = null;
	@ManyToMany
	@JoinTable(name = "school_year_subject", joinColumns = {@JoinColumn(name = "id_school_year", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_subject", nullable = false)})
	private List<SchoolYearEntity> schoolYears = new ArrayList<>();
	
	public SubjectEntity() {}

	public Integer getIdSubject() {
		return idSubject;
	}

	public void setIdSubject(Integer idSubject) {
		this.idSubject = idSubject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFundWeaklyHours() {
		return fundWeaklyHours;
	}

	public void setFundWeaklyHours(Integer fundWeaklyHours) {
		this.fundWeaklyHours = fundWeaklyHours;
	}

	public List<TeacherEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherEntity> teachers) {
		this.teachers = teachers;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
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
	
}

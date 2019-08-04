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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonPropertyOrder({"schoolUniqeNumber", "email", "schoolClass", "teacher"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolUniqeNumber", "email"}))
public class TeacherEntity extends UserEntity {
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.User.class)
	private Integer schoolUniqeNumber;
	@Column(nullable = false, unique = true)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.Teacher.class)
	private String email;
	@ManyToMany(mappedBy = "teachers")
	@JsonIgnore
	private List<SchoolEntity> schools = new ArrayList<>();
	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonIgnore
	private List<StudentEntity> students = new ArrayList<>();
	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "teacher_subject", joinColumns = {@JoinColumn(name = "id_teacher", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_subject", nullable = false)})
	private List<SubjectEntity> subjects = new ArrayList<>();
	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonIgnore
	private List<EvaluationEntity> evaluations = new ArrayList<>();
	
	public TeacherEntity() {}

	public Integer getSchoolUniqeNumber() {
		return schoolUniqeNumber;
	}

	public void setSchoolUniqeNumber(Integer schoolUniqeNumber) {
		this.schoolUniqeNumber = schoolUniqeNumber;
	}
	
	public List<SchoolEntity> getSchools() {
		return schools;
	}

	public void setSchools(List<SchoolEntity> schools) {
		this.schools = schools;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public List<SubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectEntity> subjects) {
		this.subjects = subjects;
	}

	public List<EvaluationEntity> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<EvaluationEntity> evaluations) {
		this.evaluations = evaluations;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

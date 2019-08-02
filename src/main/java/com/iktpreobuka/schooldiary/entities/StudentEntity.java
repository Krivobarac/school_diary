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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonPropertyOrder({"schoolUniqeNumber", "school", "schoolClass", "teacher", "schoolYear"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolUniqeNumber"}))
public class StudentEntity extends UserEntity{
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.User.class)
	private Integer schoolUniqeNumber;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private SchoolEntity school;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private ClassEntity schoolClass;
	@ManyToMany(mappedBy = "students")
	private List<ParentEntity> parents = new ArrayList<>();
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private TeacherEntity teacher;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private SchoolYearEntity schoolYear;
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<EvaluationEntity> evaluations = new ArrayList<>();
	
	public StudentEntity() {}

	public Integer getSchoolUniqeNumber() {
		return schoolUniqeNumber;
	}

	public void setSchoolUniqeNumber(Integer schoolUniqeNumber) {
		this.schoolUniqeNumber = schoolUniqeNumber;
	}

	public SchoolEntity getSchool() {
		return school;
	}

	public void setSchool(SchoolEntity school) {
		this.school = school;
	}

	public List<ParentEntity> getParents() {
		return parents;
	}

	public void setParents(List<ParentEntity> parent) {
		this.parents = parent;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public ClassEntity getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(ClassEntity schoolClass) {
		this.schoolClass = schoolClass;
	}

	public SchoolYearEntity getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYearEntity schoolYear) {
		this.schoolYear = schoolYear;
	}

	public List<EvaluationEntity> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<EvaluationEntity> evaluations) {
		this.evaluations = evaluations;
	}
	
}

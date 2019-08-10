package com.iktpreobuka.schooldiary.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IClass;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolUniqeNumber"}))
public class StudentEntity extends UserEntity{
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.User.class)
	private Long schoolUniqeNumber;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_school", nullable = false)
	@JsonManagedReference
	private SchoolEntity school;
	@NotNull(message = "Razred je obavezan!")
	private IClass grade;
	@JsonIgnore
	@OneToMany(mappedBy = "students", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<ClassDepartmentEntity> classDepartments;
	@NotNull(message = "Obavezan unos roditelja!")
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = {"id_student", "id_parent"}), name = "student_parent", joinColumns = {@JoinColumn(name = "id_student", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_parent", nullable = false)})
	@JsonBackReference
	private List<ParentEntity> parents = new ArrayList<>();
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@NotNull(message = "Obavezan unos skolske godine!")
	@JoinColumn(name = "id_schoolYear", nullable = false)
	@JsonManagedReference
	private SchoolYearEntity schoolYear;
	@JsonBackReference
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<EvaluationEntity> evaluations = new ArrayList<>();
	
	public StudentEntity() {}

	public StudentEntity(String firstName, String lastName, String jmbg, IGender gender, AccountEntity account, AddressEntity address, Long schoolUniqeNumber, SchoolEntity school, IClass grade, SchoolYearEntity schoolYear, ParentEntity parent) {
		super(firstName, lastName, jmbg,  gender, account, address);
		this.schoolUniqeNumber = schoolUniqeNumber;
		this.school = school;
		this.schoolYear = schoolYear;
		this.parents.add(parent);
		this.grade = grade;
	}

	public Long getSchoolUniqeNumber() {
		return schoolUniqeNumber;
	}

	public void setSchoolUniqeNumber(Long schoolUniqeNumber) {
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

	public void setParents(ParentEntity parent) {
		this.parents.add(parent);
	}

	public List<ClassDepartmentEntity> getClassDepartments() {
		return classDepartments;
	}

	public void setClassDepartments(List<ClassDepartmentEntity> classDepartments) {
		this.classDepartments = classDepartments;
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
	
	public IClass getGrade() {
		return grade;
	}

	public void setGrade(IClass grade) {
		this.grade = grade;
	}

	public void setParents(List<ParentEntity> parents) {
		this.parents = parents;
	}
	
}

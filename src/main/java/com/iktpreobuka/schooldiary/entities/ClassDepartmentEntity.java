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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.iktpreobuka.schooldiary.enums.IDepartment;
import com.iktpreobuka.schooldiary.securities.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IClass;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idClassDepartment"}))
public class ClassDepartmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	@JsonIgnore
	private Integer idClassDepartment;
	@Column(length = 11, nullable = false)
	@NotNull(message = "Razred je obavezan!")
	@JsonView(Views.User.class)
	private IClass schoolClass;
	@Column(length = 11)
	@JsonView(Views.User.class)
	private IDepartment department;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_school", nullable = false)
	@NotNull(message = "Skola je obavezna!")
	@JsonBackReference
	@JsonView(Views.User.class)
	private SchoolEntity school;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_school_year", nullable = false)
	@NotNull(message = "Skolska godina je obavezna!")
	@JsonManagedReference
	@JsonView(Views.User.class)
	private SchoolYearEntity schoolYear;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = {"class_department_id", "student_id"}), name = "class_department_student", 
    joinColumns = { @JoinColumn(name = "class_department_id", referencedColumnName = "idClassDepartment") },
    inverseJoinColumns = { @JoinColumn(name = "student_id", referencedColumnName = "id_user") })
	private List<StudentEntity> students = new ArrayList<>();
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_student", nullable = false)
	@NotNull
	@JsonBackReference
	@JsonView(Views.User.class)
	private StudentEntity student;
	@Version
	@JsonIgnore
	private Integer version;
	
	public ClassDepartmentEntity() {}

	public ClassDepartmentEntity(IClass schoolClass, IDepartment department, SchoolEntity school, SchoolYearEntity schoolYear, StudentEntity student) {
		super();
		this.schoolClass = schoolClass;
		this.department = department;
		this.school = school;
		this.schoolYear = schoolYear;
		this.students.add(student);
		this.student = student;
	}

	public Integer getIdClassDepartment() {
		return idClassDepartment;
	}

	public void setIdClassDepartment(Integer idClassDepartment) {
		this.idClassDepartment = idClassDepartment;
	}

	public IClass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(IClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public IDepartment getDepartment() {
		return department;
	}

	public void setDepartment(IDepartment department) {
		this.department = department;
	}

	public SchoolEntity getSchool() {
		return school;
	}

	public void setSchool(SchoolEntity school) {
		this.school = school;
	}

	public SchoolYearEntity getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYearEntity schoolYear) {
		this.schoolYear = schoolYear;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}
	
	public void setStudents(StudentEntity student) {
		this.students.add(student);
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
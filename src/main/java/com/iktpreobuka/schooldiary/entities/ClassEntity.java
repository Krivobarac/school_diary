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

import com.iktpreobuka.schooldiary.enums.IDepartment;
import com.iktpreobuka.schooldiary.enums.IClass;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idClass"}))
public class ClassEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	private Integer idClass;
	@Column(length = 11, nullable = false)
	private IClass schoolClass;
	@Column(length = 11, nullable = false)
	private IDepartment department;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private SchoolEntity school;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private SchoolYearEntity schoolYear;
	@OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<StudentEntity> students = new ArrayList<>();
	@ManyToMany
	@JoinTable(name = "school_class_subject", joinColumns = {@JoinColumn(name = "id_school_class", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_subject", nullable = false)})
	private List<SubjectEntity> subjects = new ArrayList<>();
	@Column(length = 11, nullable = false)
	private Integer version;
	
	public ClassEntity() {}

	public Integer getIdClass() {
		return idClass;
	}

	public void setIdClass(Integer idClass) {
		this.idClass = idClass;
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

	public List<SubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectEntity> subjects) {
		this.subjects = subjects;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
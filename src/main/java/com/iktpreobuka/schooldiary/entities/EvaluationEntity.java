package com.iktpreobuka.schooldiary.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.iktpreobuka.schooldiary.enums.IMark;
import com.iktpreobuka.schooldiary.enums.ISemester;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iktpreobuka.schooldiary.enums.IClass;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idEvalueted"}))
public class EvaluationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	private Integer idEvalueted;
	@NotNull(message = "Student je obavezan!")
	@JoinColumn(name = "id_student", nullable = false)
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private StudentEntity student;
	@NotNull(message = "Nastavnik je obavezan!")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_teacher", nullable = false)
	private TeacherEntity teacher;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_subject", nullable = false)
	@NotNull(message = "Predmet je obavezan!")
	private SubjectEntity subject;
	@Column(length = 11, nullable = false)
	@NotNull(message = "Polugodiste je obavezno!")
	private ISemester semester;
	@Column(length = 11, nullable = false)
	@NotNull(message = "Razred je obavezan!")
	private IClass schoolClass;
	@Column(length = 11, nullable = false)
	@NotNull(message = "Ocena je obavezna!")
	@Enumerated(EnumType.ORDINAL)
	private IMark mark;
	@Version
	@JsonIgnore
	private Integer version = null;
	
	public EvaluationEntity() {}
	
	public EvaluationEntity(StudentEntity student, TeacherEntity teacher, SubjectEntity subject, ISemester semester,
			IClass schoolClass, IMark mark) {
		super();
		this.student = student;
		this.teacher = teacher;
		this.subject = subject;
		this.semester = semester;
		this.schoolClass = schoolClass;
		this.mark = mark;
	}

	public Integer getIdEvalueted() {
		return idEvalueted;
	}

	public void setIdEvalueted(Integer idEvalueted) {
		this.idEvalueted = idEvalueted;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public ISemester getSemester() {
		return semester;
	}

	public void setSemester(ISemester semester) {
		this.semester = semester;
	}

	public IClass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(IClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public Integer getMark() {
		return mark.ordinal();
	}

	public void setMark(IMark mark) {
		this.mark = mark;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}

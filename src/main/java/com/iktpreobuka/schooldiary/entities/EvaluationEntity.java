package com.iktpreobuka.schooldiary.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.iktpreobuka.schooldiary.enums.IMark;
import com.iktpreobuka.schooldiary.enums.ISemester;
import com.iktpreobuka.schooldiary.enums.IClass;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idEvalueted"}))
public class EvaluationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	private Integer idEvalueted;
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private StudentEntity student;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private TeacherEntity teacher;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private SubjectEntity subject;
	@Column(length = 11, nullable = false)
	private ISemester semester;
	@Column(length = 11, nullable = false)
	private IClass schoolClass;
	@Column(length = 11, nullable = false)
	private IMark mark;
	@Version
	private Integer version = null;
	
	public EvaluationEntity() {}

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

	public IMark getMark() {
		return mark;
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

package com.iktpreobuka.schooldiary.entities;

import java.time.LocalDateTime;

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

import com.iktpreobuka.schooldiary.enums.IAbsence;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"idAbsence"}))
public class AbsenceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	private Integer idAbsence;
	@Column(length = 11, nullable = false)
	private IAbsence absence;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private SubjectEntity subject;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private StudentEntity student;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(nullable = false)
	private TeacherEntity teacher;
	@Column(nullable = false)
	private LocalDateTime time = LocalDateTime.now();
	@Version
	private Integer version = null;
	
	public AbsenceEntity() {}

	public Integer getIdAbsence() {
		return idAbsence;
	}

	public void setIdAbsence(Integer idAbsence) {
		this.idAbsence = idAbsence;
	}

	public IAbsence getAbsence() {
		return absence;
	}

	public void setAbsence(IAbsence absence) {
		this.absence = absence;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
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

	public LocalDateTime getInsertTime() {
		return time;
	}

	public void setInsertTime(LocalDateTime insertTime) {
		this.time = insertTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}

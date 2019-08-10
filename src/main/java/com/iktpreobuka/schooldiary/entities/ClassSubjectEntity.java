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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IClass;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class ClassSubjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 11, nullable = false, unique = true, updatable = false)
	@JsonView(Views.SuperAdmin.class)
	private Integer id;
	@NotNull(message = "Razred je opavezan!")
	@JoinColumn(name = "school_class", nullable = false)
	@JsonView(Views.Admin.class)
	private IClass schoolClass;
	@NotNull(message = "Predmet je obavezno!")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "subject", nullable = false)
	@JsonManagedReference
	@JsonView(Views.Admin.class)
	private SubjectEntity subject;
	@Column(length = 5, nullable = false)
	@NotNull(message = "Morate upisati nedeljni broj casova!")
	@JsonView(Views.Admin.class)
	private Integer fundWeaklyHours;
	@Version
	@JsonView(Views.SuperAdmin.class)
	private Integer version;
	
	public ClassSubjectEntity(IClass schoolClass, SubjectEntity subject, Integer fundWeaklyHours) {
		super();
		this.schoolClass = schoolClass;
		this.subject = subject;
		this.fundWeaklyHours = fundWeaklyHours;
	}

	public ClassSubjectEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public IClass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(IClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public Integer getFundWeaklyHours() {
		return fundWeaklyHours;
	}

	public void setFundWeaklyHours(Integer fundWeaklyHours) {
		this.fundWeaklyHours = fundWeaklyHours;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}

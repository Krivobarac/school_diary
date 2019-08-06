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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"schoolUniqeNumber"}))
public class TeacherEntity extends UserEntity {
	@Column(nullable = false, unique = true, length = 10)
	@JsonView(Views.User.class)
	private Long schoolUniqeNumber;
	@Column(nullable = false)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.Teacher.class)
	private String email;
	@NotNull(message = "Predmet je obavezan")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "id_subject")
	@JsonManagedReference
	private SubjectEntity subject;
	@ManyToMany()
	@JsonBackReference
	@JoinTable(uniqueConstraints = @UniqueConstraint(columnNames = {"id_teacher", "id_school"}), name = "teacher_school", joinColumns = {@JoinColumn(name = "id_teacher", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_school", nullable = false)})
	private List<SchoolEntity> schools = new ArrayList<>();
	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JsonIgnore
	private List<EvaluationEntity> evaluations = new ArrayList<>();
	
	public TeacherEntity() {}

	public TeacherEntity(String firstName, String lastName, String jmbg, IGender gender, AccountEntity account, AddressEntity address, Long schoolUniqeNumber, String email, SubjectEntity subject, SchoolEntity school) {
		super(firstName, lastName, jmbg, gender, account, address);
		this.schoolUniqeNumber = schoolUniqeNumber;
		this.email = email;
		this.subject = subject;
		this.schools.add(school);
	}

	public Long getSchoolUniqeNumber() {
		return schoolUniqeNumber;
	}

	public void setSchoolUniqeNumber(Long schoolUniqeNumber) {
		this.schoolUniqeNumber = schoolUniqeNumber;
	}
	
	public List<SchoolEntity> getSchools() {
		return schools;
	}

	public void setSchools(SchoolEntity school) {
		this.schools.add(school);
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

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

}

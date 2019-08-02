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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.securities.Views;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class ParentEntity extends UserEntity{
	@Column(nullable = false, unique = true)
	@NotNull(message = "Email je obavezan!")
	@Pattern(regexp = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Email nije ispravan!")
	@JsonView(Views.User.class)
	private String email;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "parent_student", joinColumns = {@JoinColumn(name = "id_parent", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "id_student", nullable = false)})
	private List<StudentEntity> students = new ArrayList<>();
	
	public ParentEntity() {}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

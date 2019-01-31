package com.ukw.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "classrooms", uniqueConstraints = { @UniqueConstraint(columnNames = { "classNumber", "teacherId" }) })
public class Classroom implements Serializable {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String classNumber;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "teacherId", referencedColumnName = "id")
	private User teacher;
	private String homework;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	Classroom() {
	}

	public Classroom(String classNumber) {
		this.classNumber = classNumber;
		this.homework = "";
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

}

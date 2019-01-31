package com.ukw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private float scoreMath;
	private float scoreScience;
	private float scoreWriting;
	private float scoreReading;
	private float scoreLifeSkills;
	private String studName;
	@ManyToOne(optional = true)
	@JoinColumn(name = "classRoom_id", referencedColumnName = "id", nullable = true)
	private Classroom classRoom;

	@ManyToOne(optional = true)
	@JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
	private User parent;

	Student() {
	}

	public Student(String studName, float scoreMath, float scoreScience, float scoreWriting, float scoreReading,
			float scoreLifeSkills) {
		this.studName = studName;
		this.scoreMath = scoreMath;
		this.scoreScience = scoreScience;
		this.scoreWriting = scoreWriting;
		this.scoreReading = scoreReading;
		this.scoreLifeSkills = scoreLifeSkills;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getScoreMath() {
		return scoreMath;
	}

	public void setScoreMath(float scoreMath) {
		this.scoreMath = scoreMath;
	}

	public float getScoreScience() {
		return scoreScience;
	}

	public void setScoreScience(float scoreScience) {
		this.scoreScience = scoreScience;
	}

	public float getScoreWriting() {
		return scoreWriting;
	}

	public void setScoreWriting(float scoreWriting) {
		this.scoreWriting = scoreWriting;
	}

	public float getScoreReading() {
		return scoreReading;
	}

	public void setScoreReading(float scoreReading) {
		this.scoreReading = scoreReading;
	}

	public float getScoreLifeSkills() {
		return scoreLifeSkills;
	}

	public void setScoreLifeSkills(float scoreLifeSkills) {
		this.scoreLifeSkills = scoreLifeSkills;
	}

	public String getStudName() {
		return studName;
	}

	public void setStudName(String studName) {
		this.studName = studName;
	}

	public Classroom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(Classroom classRoom) {
		this.classRoom = classRoom;
	}

	public User getParent() {
		return parent;
	}

	public void setParent(User parent) {
		this.parent = parent;
	}

}

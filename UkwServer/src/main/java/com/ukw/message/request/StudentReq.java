package com.ukw.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StudentReq {

	@NotNull
	@NotBlank
	public String studName;

	@NotNull
	@NotBlank
	public String classNumber;

	@NotNull
	@NotBlank
	public String parentUserName;

	public float scoreMath;
	public float scoreScience;
	public float scoreWriting;
	public float scoreReading;
	public float scoreLifeSkills;

	public StudentReq() {
	}

	public String getStudName() {
		return studName;
	}

	public void setStudName(String studName) {
		this.studName = studName;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getParentUserName() {
		return parentUserName;
	}

	public void setParentUserName(String parentUserName) {
		this.parentUserName = parentUserName;
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

}

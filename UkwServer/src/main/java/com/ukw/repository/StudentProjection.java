package com.ukw.repository;

public interface StudentProjection {

	public Long getId();

	public String getStudName();

	public Classname getClassRoom();

	interface Classname {
		String getClassNumber();
	}

	public ParentDetail getParent();

	interface ParentDetail {
		String getFirstName();
	}

	public float getScoreMath();

	public float getScoreScience();

	public float getScoreWriting();

	public float getScoreReading();

	public float getScoreLifeSkills();

}

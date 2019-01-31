package com.ukw.message.response;

public class ClassroomResponse {

	private String classNumber;
	private String homeWork;

	public String getHomeWork() {
		return homeWork;
	}

	public void setHomeWork(String homeWork) {
		this.homeWork = homeWork;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getTeacherUserName() {
		return teacherUserName;
	}

	public void setTeacherUserName(String teacherUserName) {
		this.teacherUserName = teacherUserName;
	}

	private String teacherUserName;

}

package com.ukw.message.request;

import javax.validation.constraints.NotNull;

public class ClassroomReq {

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

	@NotNull
	private String classNumber;
	@NotNull
	private String teacherUserName;
	
	private String homework;

	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

}

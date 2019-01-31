package com.ukw.repository;

public interface ClassRoomProjection {

	public String getHomework();

	public TeacherName getTeacher();

	interface TeacherName {
		public String getFirstName();
	}

	public String getClassNumber();

}

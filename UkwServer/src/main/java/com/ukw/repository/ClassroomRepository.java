package com.ukw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.ukw.model.Classroom;
import com.ukw.model.User;


@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

	// @Query("SELECT c FROM classroom c WHERE c.classNumber = ?1")
	// Boolean existByClassNumber(String classNumber);
	List<ClassRoomProjection> findAllProjectedBy();

	Optional<Classroom> findByClassNumber(String classNumber);

	Optional<ClassRoomProjection> findByTeacher_Id(long Id);

	<T> List<T> findAllByTeacher(User user);

	Optional<Classroom> findByTeacher(User user);

	Optional<Classroom> findByTeacherUserName(String username);

}

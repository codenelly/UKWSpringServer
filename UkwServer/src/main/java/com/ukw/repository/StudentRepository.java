package com.ukw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ukw.model.Student;



@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	/*
	 * <T>List<T> findByRoom(Classroom classroom); List<StudentProjection>
	 * findByParent_Id(Long id);
	 */

	List<StudentProjection> findByClassRoom_Id(Long id);

	List<StudentProjection> findByParent_Id(Long id);

}

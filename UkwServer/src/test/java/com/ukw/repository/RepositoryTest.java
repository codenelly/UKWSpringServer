package com.ukw.repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.ukw.model.Classroom;
import com.ukw.model.Role;
import com.ukw.model.RoleNames;
import com.ukw.model.User;
import com.ukw.repository.ClassRoomProjection;
import com.ukw.repository.ClassroomRepository;
import com.ukw.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	ClassroomRepository classroomRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void should_return_empty_user_if_no_user_with_name_exist(){

		
		Optional<User> userRes =userRepository.findByUserName("David");
		assertThat(userRes).isEmpty();
		
	}
	
	@Test
	public void should_return_user_if_user_with_name_exist(){
		
		User user1 = new User("John", "David", "johndavid123",
				"JohnD@abc.com", "pass123");
		testEntityManager.persist(user1);
		Optional<User> userRes =userRepository.findByUserName("johndavid123");
		assertThat(userRes).isNotEmpty();
		assertThat(userRes.get().getFirstName()).isEqualTo(user1.getFirstName());
	}
	
	@Test
	public void should_return_true_exist_by_user_name(){
		
		User user1 = new User("John", "David", "johndavid123",
				"JohnD@abc.com", "pass123");
		testEntityManager.persist(user1);
		Boolean bool =userRepository.existsByUserName("johndavid123");
		assertThat(bool).isTrue();
	}
	
	
	@Test
	public void should_return_true_exist_by_user_email(){
		
		User user1 = new User("John", "David", "johndavid123",
				"JohnD@abc.com", "pass123");
		testEntityManager.persist(user1);
		Boolean bool =userRepository.existsByEmail("JohnD@abc.com");
		assertThat(bool).isTrue();
	}
	
	//classReposiroy
	@Test
	public void should_return_classroom_if_exist_by_classnumber(){
		Classroom classtest = new Classroom("1A");
		testEntityManager.persist(classtest);
		Optional<Classroom> classRes =
				classroomRepository.findByClassNumber(classtest.getClassNumber());
		assertThat(classRes).isNotEmpty();
		assertThat(classRes.get().getClassNumber()).isEqualTo("1A");

		
	}
	
	@Test
	public void should_return_projectedClassroom_if_exists_by_teacher_name(){
		Classroom classtest = new Classroom("1A");
		User user1 = new User("John", "David", "johndavid123",
				"JohnD@abc.com", "pass123");
		Set<Role> roles = new HashSet<>();
		Role roleAdmin = new Role();
		roleAdmin.setRoleName(RoleNames.ROLE_TEACHER);
		roles.add(roleAdmin);
		user1.setRoles(roles);
		//user1.setId(1L);
		classtest.setTeacher(user1);
		testEntityManager.persist(user1);
		testEntityManager.persist(classtest);
		
		Optional<ClassRoomProjection> classproj = classroomRepository.findByTeacher_Id(user1.getId());
		
		assertThat(classproj).isNotEmpty();
		assertThat(classproj.get().getTeacher().getFirstName()).isEqualTo(user1.getFirstName());
		
	}
	
	
	
	
	
	
	
	

}

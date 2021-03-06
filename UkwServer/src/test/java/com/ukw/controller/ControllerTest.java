package com.ukw.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.internal.requests.ClassRequest;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


import org.apache.catalina.filters.CorsFilter;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ukw.Application;
import com.ukw.message.request.ClassroomReq;
import com.ukw.message.request.LoginForm;
import com.ukw.message.request.RegisterForm;
import com.ukw.model.Classroom;
import com.ukw.model.Role;
import com.ukw.model.RoleNames;
import com.ukw.model.User;
import com.ukw.repository.ClassroomRepository;
import com.ukw.repository.RoleRepository;
import com.ukw.repository.UserRepository;
import com.ukw.security.services.UkwUser;
import com.ukw.security.services.UserDetailsServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes ={Application.class})
@WebAppConfiguration
public class ControllerTest {
	
	  private MockMvc mvc = null;
	  
	  @Autowired
	  private WebApplicationContext context;
	  
	  @Before
	    public void setup() {
	        mvc = MockMvcBuilders
	                .webAppContextSetup(context)
	                .apply(springSecurity()) // enable security for the mock set up
	                .build();
	       
	    }
	  
	/* @MockBean
	  private AuthenticationManager authenticationManager;
	  
	  
	  @MockBean
	  private UkwTokenProvider myTokenProvider;
	  */
	  @MockBean UserDetailsServiceImpl userDetailsServiceImpl;
	  
	  @MockBean RoleRepository roleRepository;
	  @MockBean UserRepository userRepository;
	  @MockBean ClassroomRepository classroomRepository;
	  @Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

	@Test
	@WithMockUser(roles={"ADMIN"})// only admin allowed to create class
	public void preauthorise_with_right_role_createClass_test() throws Exception{
		//Mock request
		ClassroomReq newClass = new ClassroomReq();
		newClass.setClassNumber("1A");
		newClass.setTeacherUserName("user1");
		//mock teacher user with role teacher
		Classroom newClassRoom = new Classroom(newClass.getClassNumber());
		User mockUser = new User("user", "user", "user1", "user@abc.com", "pass123");
		Set<Role> setRoles = new HashSet<>();
		Role roleTeacher = new Role();
		roleTeacher.setRoleName(RoleNames.ROLE_TEACHER);
		setRoles.add(roleTeacher);
		mockUser.setRoles(setRoles);
		mockUser.setId(1L);
		Optional<User> opUser = Optional.of(mockUser);
		//mock all db calls
		Mockito.when(this.userRepository.findByUserName("user1")).thenReturn(opUser);
		Mockito.when(this.classroomRepository.save(newClassRoom)).thenReturn(newClassRoom);
		// Mockito.when(this.classroomRepository.findByTeacher_Id(1)).thenReturn(value)
		mvc.perform(post("/classes").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newClass)))
				.andExpect(status().isOk());
		
	}
	
	@Test
	@WithMockUser(roles={"ADMIN"})
	public void preauthorize_with_right_role_getClassList_test() throws Exception{
		mvc.perform(get("/classes")).andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(roles={"TEACHER"})
	public void preauthorize_with_wrong_right_role_getClassList_test() throws Exception{
		mvc.perform(get("/classes")).andExpect(status().isForbidden());
	}
	
	//to do for all controller methods

	  public static String asJsonString(final Object obj) {
		    try {
		        return new ObjectMapper().writeValueAsString(obj);
		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }
		}

}

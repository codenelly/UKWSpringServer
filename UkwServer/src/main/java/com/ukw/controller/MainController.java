package com.ukw.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ukw.message.request.LoginForm;
import com.ukw.message.request.RegisterForm;
import com.ukw.message.response.JwtResponse;
import com.ukw.model.Role;
import com.ukw.model.RoleNames;
import com.ukw.model.User;
import com.ukw.repository.ClassRoomProjection;
import com.ukw.repository.RoleRepository;
import com.ukw.repository.UserRepository;
import com.ukw.security.tokenauth.UkwTokenProvider;

import org.springframework.ui.Model;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/users/")

public class MainController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UkwTokenProvider jwtProvider;

	@GetMapping(path = "/hello")
	public String get() {
		return "hello world";
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest)
			throws NoSuchAlgorithmException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/registration")
	public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterForm signUpRequest) {
		if (userRepository.existsByUserName(signUpRequest.getUserName())) {
			
			return new ResponseEntity<String>("{\"message\":\"Username already taken\"}", HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<String>("{\"message\":\"Email is already in use!\"}", HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getUserName(),
				signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
		System.out.println("Registration Main controller" + signUpRequest.getFirstName() + signUpRequest.getRoles());
		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByRoleName(RoleNames.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(adminRole);

				break;
			case "parent":
				Role parRole = roleRepository.findByRoleName(RoleNames.ROLE_PARENT)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(parRole);

				break;
			case "teacher":
				Role teacherRole = roleRepository.findByRoleName(RoleNames.ROLE_TEACHER)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(teacherRole);
				break;

			}
		});

		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<String>("{\"message\":\"User registered successfully!\"}" ,HttpStatus.OK );
		//return ResponseEntity.ok().body("User registered successfully!");
	}
	
	
	@GetMapping("allTeachers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> getList() {
		

	List<User> classlistPro = userRepository.findBySpecificRole(RoleNames.ROLE_TEACHER);
	
	return new ResponseEntity<List<User>>(classlistPro,HttpStatus.OK );

	}

}
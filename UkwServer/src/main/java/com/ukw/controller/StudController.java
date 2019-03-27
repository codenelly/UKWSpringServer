package com.ukw.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ukw.message.request.StudentReq;
import com.ukw.model.Classroom;
import com.ukw.model.Student;
import com.ukw.model.User;
import com.ukw.repository.ClassroomRepository;
import com.ukw.repository.RoleRepository;
import com.ukw.repository.StudentProjection;
import com.ukw.repository.StudentRepository;
import com.ukw.repository.UserRepository;
import com.ukw.security.tokenauth.UkwTokenProvider;



@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/students")
public class StudController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ClassroomRepository classRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UkwTokenProvider jwtProvider;

	@Autowired
	StudentRepository studentRepository;

	@PostMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> createStudent(@Valid @RequestBody StudentReq studentReq) {

		Optional<Classroom> classNo = classRepository.findByClassNumber(studentReq.classNumber);

		if (classNo.isPresent()) {

			Student stud = new Student(studentReq.studName, 0f, 0f, 0f, 0f, 0f);
			Optional<User> user = userRepository.findByUserName(studentReq.parentUserName);
			if (user.isPresent()) {
				stud.setParent(user.get());
				stud.setClassRoom(classNo.get());
				studentRepository.save(stud);
				return new ResponseEntity<String>("{\"message\":\"Student added sucessfully!\"}",
						HttpStatus.OK);
				
			} else
				return new ResponseEntity<String>("{\"message\":\"Parent is not registered. Please register parent first!!\"}" ,
						HttpStatus.BAD_REQUEST);

		} else
			return new ResponseEntity<String>("{\"message\":\"Classroom with this classnumber does not exist!!\"}",
					HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/class")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	public ResponseEntity<?> getStudList(@RequestParam("classnumber") String classNumber) {
		Optional<Classroom> classNo = classRepository.findByClassNumber(classNumber);
		if (classNo.isPresent()) {

			List<StudentProjection> list = studentRepository.findByClassRoom_Id(classNo.get().getId());

			return ResponseEntity.ok().body(list);
		} else {
			return new ResponseEntity<String>("{\"message\":\"Classroom with this classnumber does not exist!!\"}" , HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/children")
	@PreAuthorize("hasRole('PARENT')")
	public ResponseEntity<?> getStudDetails(@AuthenticationPrincipal UserDetails userPrincipal) {
		Optional<User> user = userRepository.findByUserName(userPrincipal.getUsername());
		List<StudentProjection> list = studentRepository.findByParent_Id(user.get().getId());
		return new ResponseEntity<List<StudentProjection>>(list, HttpStatus.OK);

	}

}

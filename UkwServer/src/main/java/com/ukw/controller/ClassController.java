package com.ukw.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ukw.message.request.ClassroomReq;
import com.ukw.message.response.ClassroomResponse;
import com.ukw.model.Classroom;
import com.ukw.model.Role;
import com.ukw.model.RoleNames;
import com.ukw.model.User;
import com.ukw.repository.ClassRoomProjection;
import com.ukw.repository.ClassroomRepository;
import com.ukw.repository.RoleRepository;
import com.ukw.repository.StudentRepository;
import com.ukw.repository.UserRepository;
import com.ukw.security.tokenauth.UkwTokenProvider;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/class")
public class ClassController {
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

	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> createClass(@Valid @RequestBody ClassroomReq classroomReq) {

		Optional<User> user = userRepository.findByUserName(classroomReq.getTeacherUserName());
		if (!user.isPresent()) {
			return new ResponseEntity<String>("Teacher should be a registered user", HttpStatus.BAD_REQUEST);
		} else {

			Set<Role> roleset = user.get().getRoles();
			if ((!roleset.isEmpty())
					&& (roleset.stream().anyMatch(r -> r.getRoleName().compareTo(RoleNames.ROLE_TEACHER) == 0))) {
				if (classRepository.findByClassNumber(classroomReq.getClassNumber()).isPresent())
					return new ResponseEntity<String>(" classnumber already existsr!!)", HttpStatus.BAD_REQUEST);
				Optional<ClassRoomProjection> classProj = classRepository.findByTeacher_Id(user.get().getId());

				// if (classList != null && classList.size() > 0)
				// if(classRepository.existByTeacher(user.get()))
				if (classProj.isPresent())
					return new ResponseEntity<String>(" teacher already assigned another class!!",
							HttpStatus.BAD_REQUEST);

				Classroom newClass = new Classroom(classroomReq.getClassNumber());
				newClass.setTeacher(user.get());

				classRepository.save(newClass);
				return ResponseEntity.ok().body("Class saved Sucessfully!");

			} else {
				return new ResponseEntity<String>(" Only user having teacher role can be a teacher!!)",
						HttpStatus.BAD_REQUEST);

			}
		}
	}

	@GetMapping("/getClass/classDet")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	public ResponseEntity<?> getClassDetails(String className) {

		Optional<Classroom> classDet = classRepository.findByClassNumber(className);

		if ((classDet.isPresent()) && (classDet.get().getTeacher() != null)) {
			ClassroomResponse classRes = new ClassroomResponse();
			classRes.setClassNumber(classDet.get().getClassNumber());
			classRes.setTeacherUserName(classDet.get().getTeacher().getUserName());
			classRes.setHomeWork(classDet.get().getHomework());
			return new ResponseEntity<ClassroomResponse>(classRes, HttpStatus.OK);

		} else

			return new ResponseEntity<String>("Classdetails does not exist!!", HttpStatus.OK);

	}

	@PostMapping("/updateClass/hw")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<String> updateHomeWork(String homeWorkStr, @AuthenticationPrincipal UserDetails user) {

		Optional<Classroom> classExists = classRepository.findByTeacherUserName(user.getUsername());

		if (classExists.isPresent()) {
			classExists.get().setHomework(homeWorkStr);

			classRepository.save(classExists.get());
			return new ResponseEntity<String>("Updated Sucessfully!!", HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("Teacher is not assigned any classroom yet!!", HttpStatus.OK);
		}

	}

	@GetMapping("/getAllClass")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ClassRoomProjection>> getClassList() {

		List<ClassRoomProjection> classlistPro = classRepository.findAllProjectedBy();

		return new ResponseEntity<List<ClassRoomProjection>>(classlistPro, HttpStatus.OK);

	}

}

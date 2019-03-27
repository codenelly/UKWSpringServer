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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(path = "/classes")
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

	@PostMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> createClass(@Valid @RequestBody ClassroomReq classroomReq) {

		Optional<User> user = userRepository.findByUserName(classroomReq.getTeacherUserName());
		if (!user.isPresent()) {
			return new ResponseEntity<String>("{\"message\":\"Teacher should be a registered user\"}", HttpStatus.BAD_REQUEST);
		} else {

			Set<Role> roleset = user.get().getRoles();
			if ((!roleset.isEmpty())
					&& (roleset.stream().anyMatch(r -> r.getRoleName().compareTo(RoleNames.ROLE_TEACHER) == 0))) {
				if (classRepository.findByClassNumber(classroomReq.getClassNumber()).isPresent())
					return new ResponseEntity<String>("{\"message\":\"Classnumber already exists!!\"}", HttpStatus.BAD_REQUEST);
				Optional<ClassRoomProjection> classProj = classRepository.findByTeacher_Id(user.get().getId());

				if (classProj.isPresent())
					return new ResponseEntity<String>("{\"message\":\"Teacher already assigned another class!!\"}",
							HttpStatus.BAD_REQUEST);

				Classroom newClass = new Classroom(classroomReq.getClassNumber());
				newClass.setTeacher(user.get());

				classRepository.save(newClass);
				return new ResponseEntity<String>("{\"message\":\"Class saved Sucessfully!\"}",
						HttpStatus.OK);
				
			} else {
				return new ResponseEntity<String>("{\"message\":\" Only user having teacher role can be a teacher!!\"}",
						HttpStatus.BAD_REQUEST);

			}
		}
	}

	@GetMapping("/classdetails")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	public ResponseEntity<?> getClassDetails(@RequestParam("classname") String classname) {

		Optional<Classroom> classDet = classRepository.findByClassNumber(classname);

		if ((classDet.isPresent()) && (classDet.get().getTeacher() != null)) {
			ClassroomResponse classRes = new ClassroomResponse();
			classRes.setClassNumber(classDet.get().getClassNumber());
			classRes.setTeacherUserName(classDet.get().getTeacher().getUserName());
			classRes.setHomeWork(classDet.get().getHomework());
			return new ResponseEntity<ClassroomResponse>(classRes, HttpStatus.OK);

		} else

			return new ResponseEntity<String>("{\"message\":\" Classdetails does not exist!!\"}", HttpStatus.OK);

	}
	
	

	@PatchMapping("/hw")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<String> updateHomeWork(@RequestBody ClassroomReq modifiedHwReq, @AuthenticationPrincipal UserDetails user) {

		Optional<Classroom> classExists = classRepository.findByTeacherUserName(user.getUsername());

		if (classExists.isPresent()) {
			classExists.get().setHomework(modifiedHwReq.getHomework());

			classRepository.save(classExists.get());
			return new ResponseEntity<String>("Updated Sucessfully!!", HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("{\"message\":\" Teacher is not assigned any classroom yet!!\"}", HttpStatus.OK);
		}

	}

	@GetMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ClassRoomProjection>> getClassList() {

		List<ClassRoomProjection> classlistPro = classRepository.findAllProjectedBy();

		return new ResponseEntity<List<ClassRoomProjection>>(classlistPro, HttpStatus.OK);

	}

	@GetMapping("allTeachers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ClassRoomProjection>> getList() {

		List<ClassRoomProjection> classlistPro = classRepository.findAllProjectedBy();

		return new ResponseEntity<List<ClassRoomProjection>>(classlistPro, HttpStatus.OK);

	}
}

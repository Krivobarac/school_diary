package com.iktpreobuka.schooldiary.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.controllers.utils.ErrorMessage;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.AccountEntity;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.SubjectEntity;
import com.iktpreobuka.schooldiary.entities.TeacherEntity;
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.entities.dto.TeacherDTO;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.repositories.SubjectRepository;
import com.iktpreobuka.schooldiary.repositories.TeacherRepository;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;
import com.iktpreobuka.schooldiary.services.RoleService;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping("/schoolDiary/users/teacher")
public class TeacherController {
	
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private EmailService emailServ;
	@Autowired
	private AddressService addressServ;
	@Autowired
	private AccountService accountServ;
	@Autowired
	private RoleService roleServ;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private ErrorMessage errMsg;
	
	@Secured(value = {"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewTeacher(@Valid @RequestBody(required = false) TeacherDTO teacherDto, BindingResult result, Principal principal){
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(teacherDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_TEACHER);
		String password = teacherDto.getFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + teacherDto.getFirstName().substring(1, 2) + teacherDto.getLastName().substring(1,2);
		String userName =  teacherDto.getEmail().substring(0, teacherDto.getEmail().indexOf('@')) + "T";
		long schoolNumber = teacherDto.getSchoolNumber();
		AccountEntity account = new AccountEntity(userName, new BCryptPasswordEncoder().encode(password), role);
		AddressEntity address = new AddressEntity(new StreetEntity(teacherDto.getNameStreet()), new HouseNumberEntity(teacherDto.getHouseNumber()), new CityEntity(teacherDto.getNameCity(), new BoroughEntity(teacherDto.getNameBorough(), teacherDto.getNumberBorough())));
		try {
			Long schoolUniqeNumber = Long.parseLong((((int)teacherRepository.count())+1) + "" + (new Random().nextInt(900)+100) + "" + LocalDateTime.now().getDayOfMonth() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getYear());	
			SchoolEntity school = schoolRepository.findByNumberSchool(schoolNumber);
			SubjectEntity subject = subjectRepository.findBySubjectName(teacherDto.getSubject());
			if (school == null) {return new ResponseEntity<RestError>(new RestError(451, "Exception occurred: Skola sa datim brojem ne postoji!"), HttpStatus.BAD_REQUEST);}
			if (subject == null) {return new ResponseEntity<RestError>(new RestError(452, "Exception occurred: Morate uneti ispravan naziv predmeta!"), HttpStatus.BAD_REQUEST);}
			AccountEntity accountE = accountServ.save(account);
			AddressEntity addressE = addressServ.save(address);
			TeacherEntity teacher = new TeacherEntity(teacherDto.getFirstName(), teacherDto.getLastName(), teacherDto.getJmbg(), IGender. valueOf(teacherDto.getGender()), accountE, addressE, schoolUniqeNumber, teacherDto.getEmail(), subject, school);
			teacher = teacherRepository.save(teacher);
			if(teacher != null) { 
				emailServ.sendCredential(teacher.getEmail(), userName, password, teacher.getIdUser(), "teacher");
			}
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllTeachers() {
		try {
			Iterable<TeacherEntity> teachers = teacherRepository.findAll();
			if(!teachers.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<TeacherEntity>>(teachers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getTeacherById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<TeacherEntity>(teacherRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteTeacherById(@PathVariable Integer id) {
		try {
			TeacherEntity teacher = teacherRepository.findById(id).get();
			AccountEntity account = teacher.getAccount();
			teacher.setDeletedAt(LocalDateTime.now());
			String username = teacher.getAccount().getUserName();
			teacher.setAccount(null);
			teacherRepository.save(teacher);
			accountServ.delete(account);
			emailServ.deleteCredential(teacher.getEmail(), username);
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_ADMIN", "ROLE_TEACHER"})
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateTeacherById(@Valid @RequestBody(required = false) TeacherDTO teacherDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(teacherDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			TeacherEntity teacher = teacherRepository.findById(id).get();
			Integer addressId = teacherRepository.findById(id).get().getAddress().getIdAddress();
			SubjectEntity subject = subjectRepository.findBySubjectName(teacherDto.getSubject());
			AddressEntity address = new AddressEntity(new StreetEntity(teacherDto.getNameStreet()), new HouseNumberEntity(teacherDto.getHouseNumber()), new CityEntity(teacherDto.getNameCity(), new BoroughEntity(teacherDto.getNameBorough(), teacherDto.getNumberBorough())));
			if (subject == null) {return new ResponseEntity<RestError>(new RestError(452, "Exception occurred: Morate uneti ispravan naziv predmetaSkola sa datim brojem ne postoji!"), HttpStatus.BAD_REQUEST);}
			teacher.setFirstName(teacherDto.getFirstName());
			teacher.setLastName(teacherDto.getLastName());
			teacher.setJmbg(teacherDto.getJmbg());;
			teacher.setGender(IGender.valueOf(teacherDto.getGender()));
			teacher.setEmail(teacherDto.getEmail());
			teacher.setSubject(subject);
			address = addressServ.update(addressId, address);
			teacher.setAddress(address);
			teacher = teacherRepository.save(teacher);
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER"})
	@RequestMapping(method = RequestMethod.PUT, value = "/forgottenCredential")
	public ResponseEntity<?> forgottenCredential(@Valid @RequestBody(required = false) EmailDTO emailDto, BindingResult result) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(emailDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		String password = emailDto.getEmail().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + emailDto.getEmail().substring(1, 2) + emailDto.getEmail().substring(0,1);
		String userName =  emailDto.getEmail().substring(0, emailDto.getEmail().indexOf('@')) + "Tu";
		try {
			TeacherEntity teacher = teacherRepository.findByEmail(emailDto.getEmail());
			teacher.getAccount().setUserName(userName);
			teacher.getAccount().setPassword(password);	
			accountServ.updateById(teacher.getAccount().getIdAccount(), teacher.getAccount());
			teacher = teacherRepository.save(teacher);
			emailServ.sendGenerateCredential(teacher.getEmail(), userName, new BCryptPasswordEncoder().encode(password), teacher.getIdUser(), "teacher");
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/changeCredential/{id}")
	public ResponseEntity<?> changeCredential(@Valid @RequestBody(required = false) AccountDTO accounDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(accounDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			TeacherEntity teacher = teacherRepository.findById(id).get();
			teacher.getAccount().setPassword(new BCryptPasswordEncoder().encode(accounDto.getPassword()));
			teacher.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(teacher.getEmail(), accounDto.getUserName(), accounDto.getPassword(), teacher.getIdUser(), "teacher");
			return new ResponseEntity<TeacherEntity>(teacherRepository.save(teacher), HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/school")
	public ResponseEntity<?> getTeachersBySchool(@RequestParam(name = "schoolNumber") String number) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(Long.parseLong(number));
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Ne postoji skola sa datim brojem!"), HttpStatus.NOT_FOUND);
			}
			List<TeacherEntity> teachers = teacherRepository.findAllTeachersBySchool(school);
			if (teachers.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(410, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<TeacherEntity>>(teachers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/subject")
	public ResponseEntity<?> getTeachersBySubject(@RequestParam(name = "subjectName") String subjectName) {
		try {
			SubjectEntity subject = subjectRepository.findBySubjectName(subjectName);
			if (subject == null) {
				return new ResponseEntity<RestError>(new RestError(410, "Unesite ispravan naziv predmeta!"), HttpStatus.NOT_FOUND);
			}
			List<TeacherEntity> teachers = teacherRepository.findAllTeachersBySubject(subject);
			if (teachers.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(410, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<TeacherEntity>>(teachers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/addSchoolToTeacher/{id}")
	public ResponseEntity<?> addSchool(@PathVariable Integer id, @RequestParam Long numberSchool) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(numberSchool);
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Ne postoji skola sa datim brojem!"), HttpStatus.NOT_FOUND);
			}
			TeacherEntity teacher = teacherRepository.findById(id).get();
			teacher.setSchools(school);
			teacher = teacherRepository.save(teacher);
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nastavnik je vec dodeljen datoj skoli!"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

package com.iktpreobuka.schooldiary.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.validation.Valid;

import org.hibernate.Session;
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
import com.iktpreobuka.schooldiary.controllers.utils.ErrorMessage;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.AccountEntity;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.AdminEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.SchoolYearEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;
import com.iktpreobuka.schooldiary.entities.SubjectEntity;
import com.iktpreobuka.schooldiary.entities.TeacherEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.entities.dto.ParentDTO;
import com.iktpreobuka.schooldiary.entities.dto.StudentDTO;
import com.iktpreobuka.schooldiary.entities.dto.StudentParentDTO;
import com.iktpreobuka.schooldiary.entities.dto.TeacherDTO;
import com.iktpreobuka.schooldiary.enums.IClass;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.AdminRepository;
import com.iktpreobuka.schooldiary.repositories.ParentRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolYearRepository;
import com.iktpreobuka.schooldiary.repositories.StudentRepository;
import com.iktpreobuka.schooldiary.repositories.SubjectRepository;
import com.iktpreobuka.schooldiary.repositories.TeacherRepository;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;
import com.iktpreobuka.schooldiary.services.RoleService;
import com.iktpreobuka.schooldiary.services.UserService;

@RestController
@RequestMapping("/schoolDiary/users/student")
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private ParentRepository parentRepository;
	@Autowired
	private EmailService emailServ;
	@Autowired
	private AddressService addressServ;
	@Autowired
	private AccountService accountServ;
	@Autowired
	private UserService userServ;
	@Autowired
	private RoleService roleServ;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private ErrorMessage errMsg;
	
	@Secured(value = {"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewStudent(@Valid @RequestBody(required = false) StudentParentDTO studentParentDto, BindingResult result, Principal principal){
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(studentParentDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_USER);
		String studentPassword = studentParentDto.getFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + studentParentDto.getFirstName().substring(1, 2) + studentParentDto.getLastName().substring(1,2);
		String studentUserName =  studentParentDto.getParentEmail().substring(0, studentParentDto.getParentEmail().indexOf('@')) + "S" + studentParentDto.getJmbg().substring(studentParentDto.getJmbg().length()-3);
		String parentPassword = studentParentDto.getParentFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + studentParentDto.getParentFirstName().substring(1, 2) + studentParentDto.getParentLastName().substring(1,2);
		String parenttUserName =  studentParentDto.getParentEmail().substring(0, studentParentDto.getParentEmail().indexOf('@')) + "P" + studentParentDto.getParentJmbg().substring(studentParentDto.getJmbg().length()-3);
		long schoolNumber = studentParentDto.getSchoolNumber();
		AccountEntity studentAccount = new AccountEntity(studentUserName, new BCryptPasswordEncoder().encode(studentPassword), role);
		AccountEntity parentAccount = new AccountEntity(parenttUserName, new BCryptPasswordEncoder().encode(parentPassword), role);
		AddressEntity studentAddress = new AddressEntity(new StreetEntity(studentParentDto.getNameStreet()), new HouseNumberEntity(studentParentDto.getHouseNumber()), new CityEntity(studentParentDto.getNameCity(), new BoroughEntity(studentParentDto.getNameBorough(), studentParentDto.getNumberBorough())));
		AddressEntity parentAddress = new AddressEntity(new StreetEntity(studentParentDto.getParentNameStreet()), new HouseNumberEntity(studentParentDto.getParentHouseNumber()), new CityEntity(studentParentDto.getParentNameCity(), new BoroughEntity(studentParentDto.getParentNameBorough(), studentParentDto.getParentNumberBorough())));
		try {
			ParentEntity parent = parentRepository.findParentEntityByJmbgAndAccountNotNull(studentParentDto.getParentJmbg());
			Long schoolUniqeNumber = Long.parseLong((((int)studentRepository.count())+1) + "" + (new Random().nextInt(900)+100) + "" + LocalDateTime.now().getDayOfMonth() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getYear());	
			SchoolEntity school = schoolRepository.findByNumberSchool(schoolNumber);
			SchoolYearEntity schoolYear = schoolYearRepository.findBySchoolYear(studentParentDto.getSchoolYear());
			AddressEntity addressS = addressServ.save(studentAddress);
			AddressEntity addressP = addressServ.save(parentAddress);
			if (parent == null) {
				parentAccount = accountServ.save(parentAccount);
				parent = new ParentEntity(studentParentDto.getParentFirstName(), studentParentDto.getParentLastName(), studentParentDto.getParentJmbg(), IGender.valueOf(studentParentDto.getParentGender()), parentAccount, addressP, studentParentDto.getParentEmail());
				parent = parentRepository.save(parent);
			}
			parentPassword = "Vasa sifra za dato korisnicko ime!"; 
			AccountEntity accountS = accountServ.save(studentAccount);
			StudentEntity student = new StudentEntity(studentParentDto.getFirstName(), studentParentDto.getLastName(), studentParentDto.getJmbg(), IGender.valueOf(studentParentDto.getGender()), accountS, addressS, schoolUniqeNumber, school, IClass.values()[studentParentDto.getGrade()], schoolYear, parent);
			student = studentRepository.save(student);
			emailServ.sendStudentsAndParentsCredential(studentParentDto.getParentEmail(), parenttUserName, studentUserName, parentPassword, studentPassword, parent.getIdUser(), student.getIdUser(), "parent", "student");
			return new ResponseEntity<StudentEntity>(student, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllStudents() {
		try {
			Iterable<StudentEntity> students = studentRepository.findAll();
			if(!students.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<StudentEntity>>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<StudentEntity>(studentRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteStudentById(@PathVariable Integer id) {
		try {
			StudentEntity student = studentRepository.findById(id).get();
			AccountEntity account = student.getAccount();
			String username = student.getAccount().getUserName();
			student.setDeletedAt(LocalDateTime.now());
			student.setAccount(null);
			studentRepository.save(student);
			accountServ.delete(account);
			for(ParentEntity p : student.getParents()) {
				emailServ.deleteCredential(p.getEmail(), username);
			}
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateStudentById(@Valid @RequestBody(required = false) StudentDTO studentDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(studentDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			StudentEntity student = studentRepository.findById(id).get();
			Integer addressId = studentRepository.findById(id).get().getAddress().getIdAddress();
			AddressEntity address = new AddressEntity(new StreetEntity(studentDto.getNameStreet()), new HouseNumberEntity(studentDto.getHouseNumber()), new CityEntity(studentDto.getNameCity(), new BoroughEntity(studentDto.getNameBorough(), studentDto.getNumberBorough())));
			student.setFirstName(studentDto.getFirstName());
			student.setLastName(studentDto.getLastName());
			student.setJmbg(studentDto.getJmbg());
			student.setGender(IGender.valueOf(studentDto.getGender()));
			address = addressServ.update(addressId, address);
			student.setAddress(address);
			student = studentRepository.save(student);
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_USER"})
	@RequestMapping(method = RequestMethod.PUT, value = "/forgottenCredential/{id}")
	public ResponseEntity<?> forgottenCredential(@Valid @RequestBody(required = false) EmailDTO emailDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(emailDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		String password = emailDto.getEmail().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + emailDto.getEmail().substring(1, 2) + emailDto.getEmail().substring(0,1);
		String userName =  emailDto.getEmail().substring(0, emailDto.getEmail().indexOf('@')) + "S" + (new Random().nextInt(900)+100);
		try {
			StudentEntity student = studentRepository.findById(id).get();
			ParentEntity parent = parentRepository.findByEmailAndStudents(emailDto.getEmail(), student);
			if(parent != null) {
				UserEntity user = userServ.getById(student);
				user.getAccount().setUserName(userName);
				user.getAccount().setPassword(new BCryptPasswordEncoder().encode(password));	
				accountServ.updateById(user.getAccount().getIdAccount(), user.getAccount());
				emailServ.sendGenerateCredential(parent.getEmail(), userName, password, parent.getIdUser(), "student");
			}
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_USER"})
	@RequestMapping(method = RequestMethod.PUT, value = "/changeCredential/{id}")
	public ResponseEntity<?> changeCredential(@Valid @RequestBody(required = false) AccountDTO accounDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(accounDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			StudentEntity student = studentRepository.findById(id).get();
			student.getAccount().setPassword(accounDto.getPassword());
			student.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(student.getParents().get(0).getEmail(), accounDto.getUserName(), accounDto.getPassword(), student.getIdUser(), "student");
			return new ResponseEntity<StudentEntity>(studentRepository.save(student), HttpStatus.OK);
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
	public ResponseEntity<?> getAllStudentsBySchool(@RequestParam(name = "schoolNumber") String number) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(Long.parseLong(number));
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Ne postoji skola sa datim brojem!"), HttpStatus.NOT_FOUND);
			}
			List<StudentEntity> students = studentRepository.findBySchool(school);
			if (students.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(410, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/active/school")
	public ResponseEntity<?> getActiveStudentsBySchool(@RequestParam(name = "schoolNumber") String number) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(Long.parseLong(number));
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Ne postoji skola sa datim brojem!"), HttpStatus.NOT_FOUND);
			}
			List<StudentEntity> students = studentRepository.findBySchoolAndAccountNotNull(school);
			if (students.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(410, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.POST, value = "/setOtherParrent/{id}")
	public ResponseEntity<?> addOtherParent(@Valid @RequestBody(required = false) ParentDTO parentDto, BindingResult result, @PathVariable Integer id, Principal principal) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(parentDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_USER);
			StudentEntity student = studentRepository.findById(id).get();
			String parentPassword = parentDto.getParentFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + parentDto.getParentFirstName().substring(1, 2) + parentDto.getParentLastName().substring(1,2);
			String parentUserName =  parentDto.getParentEmail().substring(0, parentDto.getParentEmail().indexOf('@')) + "P" + parentDto.getParentJmbg().substring(parentDto.getParentJmbg().length()-3);
			AccountEntity parentAccount = new AccountEntity(parentUserName, new BCryptPasswordEncoder().encode(parentPassword), role);
			AddressEntity parentAddress = new AddressEntity(new StreetEntity(parentDto.getParentNameStreet()), new HouseNumberEntity(parentDto.getParentHouseNumber()), new CityEntity(parentDto.getParentNameCity(), new BoroughEntity(parentDto.getParentNameBorough(), parentDto.getParentNumberBorough())));
			ParentEntity parent = parentRepository.findParentEntityByJmbgAndAccountNotNull(parentDto.getParentJmbg());
			parentAddress = addressServ.save(parentAddress);
			if (parent == null) {
				parentAccount = accountServ.save(parentAccount);
				parent = new ParentEntity(parentDto.getParentFirstName(), parentDto.getParentLastName(), parentDto.getParentJmbg(), IGender.valueOf(parentDto.getParentGender()), parentAccount, parentAddress, parentDto.getParentEmail());
				parent = parentRepository.save(parent);
				student.getParents().add(parent);
			}
			parentPassword = "Vasa sifra za dato korisnicko ime!"; 
			student = studentRepository.save(student);
			emailServ.sendStudentsAndParentsCredential(parentDto.getParentEmail(), parentUserName, null, parentPassword, null, parent.getIdUser(), student.getIdUser(), "parent", "student");
			return new ResponseEntity<StudentEntity>(student, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}

package com.iktpreobuka.schooldiary.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.AdminEntity;
import com.iktpreobuka.schooldiary.entities.EvaluationEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;
import com.iktpreobuka.schooldiary.entities.TeacherEntity;
import com.iktpreobuka.schooldiary.enums.IMark;
import com.iktpreobuka.schooldiary.enums.ISemester;
import com.iktpreobuka.schooldiary.repositories.AdminRepository;
import com.iktpreobuka.schooldiary.repositories.EvaluationRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.repositories.StudentRepository;
import com.iktpreobuka.schooldiary.repositories.TeacherRepository;
import com.iktpreobuka.schooldiary.services.EmailService;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping(value = "/schoolDiary/evaluation")
public class EvaluationController {
	
	@Autowired
	private EvaluationRepository evaluationRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private EmailService emailServ;

	@RequestMapping(method = RequestMethod.POST, value = "/student/{id}")
	@Secured({"ROLE_TEACHER"})
	public ResponseEntity<?> addNewMark(@RequestParam Integer mark, @PathVariable Integer id, Principal principal) {
		try {
			StudentEntity student = studentRepository.findById(id).get();
			TeacherEntity teacher =  teacherRepository.findByAccountUserName(principal.getName());
			ISemester semester = (LocalDateTime.now().getMonth().getValue() > 8 && LocalDateTime.now().getMonth().getValue() < 1) ? ISemester.Prvo : ISemester.Drugo;
			IMark marked = IMark.values()[mark];
			SchoolEntity school = schoolRepository.findByStudents(student);
			List<SchoolEntity> schools = schoolRepository.findByTeachersUserName(principal.getName());
			Boolean isSet = false;
			for (SchoolEntity s : schools) {
				if (s.equals(school)) {isSet = true;}
			}
			if(!isSet) {return new ResponseEntity<RestError>(new RestError(404, "Niste zaposleni u datoj skoli!"), HttpStatus.NOT_FOUND);}
			EvaluationEntity evaluation = new EvaluationEntity(student, teacher, teacher.getSubject(), semester, student.getGrade(), marked);
			evaluation = evaluationRepository.save(evaluation);
			emailServ.sendMark(student.getParents().get(0).getEmail(), student.getIdUser());
			return new ResponseEntity<EvaluationEntity>(evaluation, HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: Ocenu morate upisati kao broj od 1-5!"), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_TEACHER", "ROLE_ADMIN", "ROLE_STUDENT", "ROLE_PARRENT"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllStudents(Authentication authentication) {
		try {
			List<EvaluationEntity> evaluations = new ArrayList<>();
			if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				AdminEntity admin = adminRepository.findByAccountUserName(authentication.getName());
				evaluations = evaluationRepository.findByStudentSchoolAdmins(admin);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_TEACHER]")) {
				TeacherEntity teacher = teacherRepository.findByAccountUserName(authentication.getName());
				evaluations = evaluationRepository.findByTeacher(teacher);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_SUPER_ADMIN]")) {
				evaluations = (List<EvaluationEntity>) evaluationRepository.findAll();
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_USER]")) {
				evaluations = evaluationRepository.findByStudentParentsAccountUserName(authentication.getName());
				if (evaluations.size() == 0) {
					evaluations = evaluationRepository.findByStudentAccountUserName(authentication.getName());
				}
			}
			if(!evaluations.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<EvaluationEntity>>(evaluations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_TEACHER", "ROLE_ADMIN", "ROLE_STUDENT", "ROLE_PARRENT", "ROLE_SUPERADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/student/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable Integer id, Authentication authentication) {
		try {
			StudentEntity student = studentRepository.findById(id).orElse(null);
			if(student == null) {return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata za datog studenta!"), HttpStatus.NOT_FOUND);}
			List<EvaluationEntity> evaluations = new ArrayList<>();
			if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				AdminEntity admin = adminRepository.findByAccountUserName(authentication.getName());
				evaluations = evaluationRepository.findByStudentAndStudentSchoolAdmins(student, admin);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_TEACHER]")) {
				TeacherEntity teacher = teacherRepository.findByAccountUserName(authentication.getName());
				evaluations = evaluationRepository.findByStudentAndTeacher(student, teacher);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_SUPER_ADMIN]")) {
				evaluations = (List<EvaluationEntity>) evaluationRepository.findByStudent(student);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_USER]")) {
				evaluations = (List<EvaluationEntity>) evaluationRepository.findByStudent(student);
			}
			if(!evaluations.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<EvaluationEntity>>(evaluations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_TEACHER", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/student/{id}/evaluation/{idE}")
	public ResponseEntity<?> changeMarkToStudent(@PathVariable Integer id, @PathVariable Integer idE, @RequestParam Integer mark, Authentication authentication) {
		try {
			StudentEntity student = studentRepository.findById(id).orElse(null);
			if(student == null) {return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata za datog studenta!"), HttpStatus.NOT_FOUND);}
			EvaluationEntity evaluation = null;
			AdminEntity admin =  null;
			if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				admin = adminRepository.findByAccountUserName(authentication.getName());
				evaluation = evaluationRepository.findByIdEvaluetedAndStudentAndStudentSchoolAdmins(idE, student, admin);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_TEACHER]")) {
				TeacherEntity teacher = teacherRepository.findByAccountUserName(authentication.getName());
				evaluation = evaluationRepository.findByIdEvaluetedAndStudentAndTeacher(idE, student, teacher);
			}
			if(evaluation == null) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			evaluation.setMark(IMark.values()[mark]);
			evaluation = evaluationRepository.save(evaluation);
			if (admin != null) {
				emailServ.sendUpdatedMark(evaluation.getTeacher().getEmail(), student.getIdUser());
			}
			emailServ.sendUpdatedMark(student.getParents().get(0).getEmail(), student.getIdUser());
			if(!student.getParents().get(1).equals(null)) {
				emailServ.sendUpdatedMark(student.getParents().get(1).getEmail(), student.getIdUser());
			}
			return new ResponseEntity<EvaluationEntity>(evaluation, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: Ocenu morate upisati kao broj od 1-5!"), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} 
	
	@Secured(value = {"ROLE_TEACHER", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/student/{idS}")
	public ResponseEntity<?> deleteEvaluation(@PathVariable Integer id, @PathVariable Integer idS, Authentication authentication) {
		StudentEntity student = studentRepository.findById(idS).orElse(null);
		if(student == null) {return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata za datog studenta!"), HttpStatus.NOT_FOUND);}
		try {
			EvaluationEntity evaluation = null;
			AdminEntity admin =  null;
			if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
				admin = adminRepository.findByAccountUserName(authentication.getName());
				evaluation = evaluationRepository.findByIdEvaluetedAndStudentAndStudentSchoolAdmins(id, student, admin);
			}
			if(authentication.getAuthorities().toString().equals("[ROLE_TEACHER]")) {
				TeacherEntity teacher = teacherRepository.findByAccountUserName(authentication.getName());
				evaluation = evaluationRepository.findByIdEvaluetedAndStudentAndTeacher(id, student, teacher);
			}
			if(evaluation == null) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			evaluationRepository.delete(evaluation);
			if (admin != null) {
				emailServ.sendUpdatedMark(evaluation.getTeacher().getEmail(), student.getIdUser());
			}
			emailServ.sendDeletedMark(student.getParents().get(0).getEmail(), student.getIdUser());
			if(!student.getParents().get(1).equals(null)) {
				emailServ.sendDeletedMark(student.getParents().get(1).getEmail(), student.getIdUser());
			}
			return new ResponseEntity<EvaluationEntity>(evaluation, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: Ocenu morate upisati kao broj od 1-5!"), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

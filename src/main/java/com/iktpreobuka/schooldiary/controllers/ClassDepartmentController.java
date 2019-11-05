package com.iktpreobuka.schooldiary.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.ClassDepartmentEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.SchoolYearEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;
import com.iktpreobuka.schooldiary.enums.IDepartment;
import com.iktpreobuka.schooldiary.repositories.ClassDepartmentRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolYearRepository;
import com.iktpreobuka.schooldiary.repositories.StudentRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping("/schoolDiary/classDepartment")
public class ClassDepartmentController {
	
	@Autowired
	private ClassDepartmentRepository classDepartmentRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	
	
	@Secured(value = {"ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addStudentToClassDepartment (@RequestParam Long numberSchool) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(numberSchool);
			if (school == null) {return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Skola sa datim brojem ne postoji!"), HttpStatus.BAD_REQUEST);}
			SchoolYearEntity schoolYear = schoolYearRepository.findBySchoolYear(LocalDateTime.now().getYear() + "/" + (LocalDateTime.now().getYear()+1));
			List<StudentEntity> students = studentRepository.findBySchoolAndAccountNotNull(school);
			List<ClassDepartmentEntity> classDepartments = new ArrayList<>();
			List<List<StudentEntity>> listOfStudentList = new ArrayList<List<StudentEntity>>();
			
			for(Integer g = 1; g < 9; g++)  {
				List<StudentEntity> tempStudents = new ArrayList<>();
				for(Integer i = 0; i < students.size(); i++) {
					if(g == students.get(i).getGrade().ordinal()) {
						tempStudents.add(students.get(i));
					}
				}
				listOfStudentList.add(tempStudents);
			}
			
			for(Integer g = 1; g < 9; g++)  {
				Integer s = 0;
				if (listOfStudentList.get(g-1).size() != 0) {
				Integer departmentNumber = (Math.round(listOfStudentList.get(g-1).size() / 30)+1);
					for(Integer i = 0; i < listOfStudentList.get(g-1).size() / departmentNumber; i++)  {
						listOfStudentList.get(g-1).get(s);
						ClassDepartmentEntity classDepartment = new ClassDepartmentEntity(listOfStudentList.get(g-1).get(s).getGrade(), IDepartment.values()[i+1], school, schoolYear, listOfStudentList.get(g-1).get(s));
						classDepartment = classDepartmentRepository.save(classDepartment);
						classDepartments.add(classDepartment);
						s++;
					}	
				}
			}
			if (classDepartments.size() == 0) {return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);}
			return new ResponseEntity<List<ClassDepartmentEntity>>(classDepartments, HttpStatus.CREATED); 
		}
		catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Jedan od ucenika je u vec kreiranom odeljenju!"), HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllClassDepartment() {
		try {
			Iterable<ClassDepartmentEntity> classDepartment = classDepartmentRepository.findAll();
			if(!classDepartment.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<ClassDepartmentEntity>>(classDepartment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getClassDepartmentById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<ClassDepartmentEntity>(classDepartmentRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/byNumberSchool")
	public ResponseEntity<?> getAllclassDepartmentbySchool(@RequestParam String numberSchool) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(Long.parseLong(numberSchool));
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(404, "Ne postoji skola sa datim brojem!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<ClassDepartmentEntity>>(classDepartmentRepository.findBySchool(school), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/bySchoolYear")
	public ResponseEntity<?> getAllClassDepartmentBySchoolYear(@RequestParam String schoolYear) {
		try {
			SchoolYearEntity schoolYearE = schoolYearRepository.findBySchoolYear(schoolYear);
			if (schoolYearE == null) {
				return new ResponseEntity<RestError>(new RestError(404, "Ne postoji data skolska godina!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<ClassDepartmentEntity>>(classDepartmentRepository.findBySchoolYear(schoolYearE), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

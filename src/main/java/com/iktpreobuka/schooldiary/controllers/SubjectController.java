package com.iktpreobuka.schooldiary.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.controllers.utils.CustomValidator;
import com.iktpreobuka.schooldiary.controllers.utils.ErrorMessage;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.ClassSubjectEntity;
import com.iktpreobuka.schooldiary.entities.SubjectEntity;
import com.iktpreobuka.schooldiary.entities.dto.ClassSubjectDTO;
import com.iktpreobuka.schooldiary.entities.dto.SubjectDTO;
import com.iktpreobuka.schooldiary.enums.IClass;
import com.iktpreobuka.schooldiary.repositories.ClassSubjectRepository;
import com.iktpreobuka.schooldiary.repositories.SubjectRepository;
import com.iktpreobuka.schooldiary.securities.Views;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping(value = "/schoolDiary/subjects")
public class SubjectController {
	
	@Autowired
	private ClassSubjectRepository classSubjectRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private ErrorMessage errMsg;
	@Autowired
	private CustomValidator validator;
	
	protected void InitBinder(final WebDataBinder binder) {
		binder.addValidators(validator);
	}
	
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.POST)
	@Secured("ROLE_SUPERADMIN")
	public ResponseEntity<?> AddSubjects(@Valid @RequestBody(required = false) List<SubjectDTO> subjectDtoArr, BindingResult result) {
		
		if(subjectDtoArr == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		List<SubjectDTO> subjectsDtos = new ArrayList<>();
		List<SubjectEntity> subjects = new ArrayList<>();
		try {
			for (SubjectDTO subjectDto: subjectDtoArr) {
				SubjectDTO.orderNumber++;
				validator.validate(subjectDto, result);
				subjectDto = new SubjectDTO(subjectDto.getSubjectName(), subjectDto.getOptional(), subjectDto.getClassList());
				subjectsDtos.add(subjectDto);
				for (ClassSubjectDTO classSubjectDto : subjectDto.getClassList()) {
					ClassSubjectDTO.orderNumber++;
					validator.validate(classSubjectDto, result);
					//ClassSubjectDTO classList = new ClassSubjectDTO(classSubjectDto.getSchoolClass(), classSubjectDto.getFundWeaklyHours());
				}
				ClassSubjectDTO.orderNumber = 0;
		    }
			SubjectDTO.orderNumber = 0;
			if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}

			for(SubjectDTO subject : subjectsDtos) {
				SubjectEntity subjectE = subjectRepository.save(new SubjectEntity(subject.getSubjectName(), subject.getOptional()));

				for(ClassSubjectDTO classList : subject.getClassList()) {
					classSubjectRepository.save(new ClassSubjectEntity(IClass.values()[Integer.parseInt(classList.getSchoolClass())], subjectE, classList.getFundWeaklyHours()));
				}
				subjects.add(subjectE);
			}
			return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Jedan od predmeta je vec unet!"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@Secured("ROLE_SUPERADMIN")
	public ResponseEntity<?> correctSubjectById(@Valid @RequestBody(required = false) SubjectEntity subjectE, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(subjectE == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			SubjectEntity subject = subjectRepository.findById(id).get();
			subject.setSubjectName(subjectE.getSubjectName());
			subject.setOptional(subjectE.getOptional());
			return new ResponseEntity<SubjectEntity>(subjectRepository.save(subject), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Predmet je vec unet"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/activity/{id}")
	@Secured("ROLE_SUPERADMIN")
	public ResponseEntity<?> changeSubjectActivity(@Valid @RequestParam(required = false) Boolean activity, @PathVariable Integer id) {
		if(activity == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			SubjectEntity subject = subjectRepository.findById(id).get();
			subject.setIsActive(activity);
			return new ResponseEntity<SubjectEntity>(subjectRepository.save(subject), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllSubjets() {
		try {
			Iterable<SubjectEntity> subjects = subjectRepository.findAll();
			if(!subjects.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getSubjectById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<SubjectEntity>(subjectRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/activity")
	public ResponseEntity<?> getSubjectsByActivity(@RequestParam(required = false) Boolean activity) {
		if(activity == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			List<SubjectEntity> subjects = subjectRepository.findByIsActive(activity);
			if(subjects.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/optionality")
	public ResponseEntity<?> getSubjectsByOptionality(@RequestParam(required = false) Boolean optionality) {
		if(optionality == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			List<SubjectEntity> subjects = subjectRepository.findByOptional(optionality);
			if(subjects.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

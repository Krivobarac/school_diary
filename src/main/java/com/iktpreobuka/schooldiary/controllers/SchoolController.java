package com.iktpreobuka.schooldiary.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.DirectorEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.dto.SchoolDTO;
import com.iktpreobuka.schooldiary.repositories.DirectorRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.services.AddressService;

@RestController
@RequestMapping(value = "/schoolDiary/schools")
public class SchoolController {
	@Autowired
	DirectorRepository directorRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private AddressService addressServ;
	@Autowired
	private ErrorMessage errMsg;
	
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.POST)
	@Secured("ROLE_SUPER_ADMIN")
	public ResponseEntity<?> addNewSchool(@Valid @RequestBody(required = false) SchoolDTO schoolDto, BindingResult result) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(schoolDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		Long schoolNumber = Long.parseLong((((int)schoolRepository.count())+1) + "" + (new Random().nextInt(900)+100) + "" + LocalDateTime.now().getDayOfMonth() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getYear());
		
		try {
			AddressEntity address = addressServ.save(new AddressEntity(new StreetEntity(schoolDto.getNameStreet()), new HouseNumberEntity(schoolDto.getHouseNumber()), new CityEntity(schoolDto.getNameCity(), new BoroughEntity(schoolDto.getNameBorough(), schoolDto.getNumberBorough()))));
			SchoolEntity school = new SchoolEntity(schoolNumber, schoolDto.getNameSchool(), address);
			return new ResponseEntity<SchoolEntity>(schoolRepository.save(school), HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_USER"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllSchools() {
		try {
			Iterable<SchoolEntity> schools = schoolRepository.findAll();
			if(!schools.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<SchoolEntity>>(schools, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_USER"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getSchoolById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<SchoolEntity>(schoolRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@Secured("ROLE_SUPER_ADMIN")
	public ResponseEntity<?> deleteSchoolById(@PathVariable Integer id) {
		try {
			SchoolEntity school = schoolRepository.findById(id).get();
			AddressEntity address = addressServ.getById(school.getAddress().getIdAddress());
			school.setAddress(address);
			schoolRepository.delete(school);
			addressServ.delete(school.getAddress());
			return new ResponseEntity<SchoolEntity>(school, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@Secured("ROLE_SUPER_ADMIN")
	public ResponseEntity<?> updateSchoolById(@Valid @RequestBody(required = false) SchoolDTO schoolDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(schoolDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			SchoolEntity school = schoolRepository.findById(id).get();
			Integer addressId = schoolRepository.findById(id).get().getAddress().getIdAddress();
			school.setNameSchool(schoolDto.getNameSchool());
			AddressEntity address = new AddressEntity(new StreetEntity(schoolDto.getNameStreet()), new HouseNumberEntity(schoolDto.getHouseNumber()), new CityEntity(schoolDto.getNameCity(), new BoroughEntity(schoolDto.getNameBorough(), schoolDto.getNumberBorough())));
			addressServ.update(addressId, address);
			return new ResponseEntity<SchoolEntity>(schoolRepository.save(school), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/findByCity")
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_USER"})
	public ResponseEntity<?> findSchoolsbyCity(@RequestParam String cityName) {
		try {
			List<SchoolEntity> schools = schoolRepository.findSchoolsByCityName(cityName);
			if(schools.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/findByBorough")
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_USER"})
	public ResponseEntity<?> findSchoolsbyBorough(@RequestParam String boroughName) {
		try {
			List<SchoolEntity> schools = schoolRepository.findSchoolsByBorough(boroughName);
			if(schools.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/schoolNoDirector")
	@Secured({"ROLE_SUPER_ADMIN"})
	public ResponseEntity<?> getSchoolsNoDirectors() {
		try {
			List<SchoolEntity> schools = schoolRepository.findSchoolsNoDirector();
			if(schools.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<SchoolEntity>>(schools, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

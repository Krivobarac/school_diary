package com.iktpreobuka.schooldiary.controllers;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Random;

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
import com.iktpreobuka.schooldiary.controllers.utils.ErrorMessage;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.AccountEntity;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.DirectorEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.DirectorDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.DirectorRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;
import com.iktpreobuka.schooldiary.services.RoleService;
import com.iktpreobuka.schooldiary.services.UserService;

@RestController
@RequestMapping("/schoolDiary/users/director")
public class DirectorController {
	@Autowired
	private DirectorRepository directorRepository;
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
	private ErrorMessage errMsg;
	
	@Secured(value = {"ROLE_SUPER_ADMIN"})
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewDirector(@Valid @RequestBody(required = false) DirectorDTO directorDto, BindingResult result){
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(directorDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_ADMIN);
		String password = directorDto.getFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + directorDto.getFirstName().substring(1, 2) + directorDto.getLastName().substring(1,2);
		String userName =  directorDto.getEmail().substring(0, directorDto.getEmail().indexOf('@')) + "D";
		long schoolNumber = directorDto.getSchoolNumber();
		try {
			Long schoolUniqeNumber = Long.parseLong((((int)directorRepository.count())+1) + "" + (new Random().nextInt(900)+100) + "" + LocalDateTime.now().getDayOfMonth() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getYear());	
			SchoolEntity school = schoolRepository.findByNumberSchool(schoolNumber);
			if (school == null) {return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Skola sa datim brojem ne postoji!"), HttpStatus.BAD_REQUEST);}
			AddressEntity address = addressServ.save(new AddressEntity(new StreetEntity(directorDto.getNameStreet()), new HouseNumberEntity(directorDto.getHouseNumber()), new CityEntity(directorDto.getNameCity(), new BoroughEntity(directorDto.getNameBorough(), directorDto.getNumberBorough()))));
			AccountEntity account = new AccountEntity(userName, new BCryptPasswordEncoder().encode(password), role);
			account = accountServ.save(account);
			DirectorEntity director = directorRepository.save(new DirectorEntity(directorDto.getFirstName(), directorDto.getLastName(), directorDto.getJmbg(), IGender. valueOf(directorDto.getGender()), account, address, directorDto.getEmail(), schoolUniqeNumber, school));
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN"})
	@JsonView(Views.SuperAdmin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllDirectors() {
		try {
			Iterable<DirectorEntity> directors = directorRepository.findAll();
			if(!directors.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<DirectorEntity>>(directors, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getDirectorById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<DirectorEntity>(directorRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteDirectorById(@PathVariable Integer id) {
		try {
			DirectorEntity director = directorRepository.findById(id).get();
			directorRepository.delete(director);
			accountServ.delete(director.getAccount());
			addressServ.delete(director.getAddress());
			emailServ.deleteCredential(director.getEmail());
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateDirectorById(@Valid @RequestBody(required = false) DirectorDTO directorDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(directorDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			DirectorEntity director = directorRepository.findById(id).get();
			Integer addressId = directorRepository.findById(id).get().getAddress().getIdAddress();
			AddressEntity address = new AddressEntity(new StreetEntity(directorDto.getNameStreet()), new HouseNumberEntity(directorDto.getHouseNumber()), new CityEntity(directorDto.getNameCity(), new BoroughEntity(directorDto.getNameBorough(), directorDto.getNumberBorough())));
			director.setFirstName(directorDto.getFirstName());
			director.setLastName(directorDto.getLastName());
			director.setJmbg(directorDto.getJmbg());;
			director.setGender(IGender.valueOf(directorDto.getGender()));
			director.setEmail(directorDto.getEmail());
			address = addressServ.update(addressId, address);
			director.setAddress(address);
			director = directorRepository.save(director);
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/forgottenCredential")
	public ResponseEntity<?> forgottenCredential(@Valid @RequestBody(required = false) EmailDTO emailDto, BindingResult result) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(emailDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		String password = emailDto.getEmail().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + emailDto.getEmail().substring(1, 2) + emailDto.getEmail().substring(0,1);
		String userName =  emailDto.getEmail().substring(0, emailDto.getEmail().indexOf('@')) + "SA";
		try {
			DirectorEntity director = directorRepository.findByEmail(emailDto.getEmail());
			UserEntity user = userServ.getById(director);
			user.getAccount().setUserName(userName);
			user.getAccount().setPassword(password);	
			accountServ.updateById(user.getAccount().getIdAccount(), user.getAccount());
			emailServ.sendGenerateCredential(director.getEmail(), userName, password, director.getIdUser());
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
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
			DirectorEntity director = directorRepository.findById(id).get();
			director.getAccount().setPassword(accounDto.getPassword());
			director.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(director.getEmail(), accounDto.getUserName(), accounDto.getPassword(), director.getIdUser());
			return new ResponseEntity<DirectorEntity>(directorRepository.save(director), HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_USER"})
	@RequestMapping(method = RequestMethod.GET, value = "/school")
	public ResponseEntity<?> getDirectorBySchool(@RequestParam(name = "idSchool") String id) {
		try {
			SchoolEntity school = schoolRepository.findById(Integer.parseInt(id)).orElse(null);
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Uneli ste nepostojecu skolu!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<DirectorEntity>(directorRepository.findBySchool(school), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/changeSchool")
	public ResponseEntity<?> changeDirectorForSchool(@RequestParam Integer idD, @RequestParam Long numberSchool) {
		try {
			SchoolEntity school = schoolRepository.findByNumberSchool(numberSchool);
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Uneli ste nepostojecu skolu!"), HttpStatus.NOT_FOUND);
			}
			DirectorEntity oldDirector = directorRepository.findBySchool(school);
			directorRepository.delete(oldDirector);
			DirectorEntity director = directorRepository.findById(idD).get();
			director.setSchool(school);
			director = directorRepository.save(director);
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata!"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	 
}

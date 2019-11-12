package com.iktpreobuka.schooldiary.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

import com.github.rozidan.springboot.logger.Loggable;
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
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.DirectorDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.entities.dto.UserInfoDTO;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.DirectorRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;
import com.iktpreobuka.schooldiary.services.RoleService;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
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
	private RoleService roleServ;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private ErrorMessage errMsg;
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewDirector(@Valid @RequestBody(required = false) DirectorDTO directorDto, BindingResult result){
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(directorDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_DIRECTOR);
		String password = directorDto.getFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + directorDto.getFirstName().substring(1, 2) + directorDto.getLastName().substring(1,2);
		String userName =  directorDto.getEmail().substring(0, directorDto.getEmail().indexOf('@')) + "D";
		long schoolNumber = directorDto.getSchoolNumber();
		AccountEntity account = new AccountEntity(userName, password, role);
		AddressEntity address = new AddressEntity(new StreetEntity(directorDto.getNameStreet()), new HouseNumberEntity(directorDto.getHouseNumber()), new CityEntity(directorDto.getNameCity(), new BoroughEntity(directorDto.getNameBorough(), directorDto.getNumberBorough())));
		try {
			Long schoolUniqeNumber = Long.parseLong((((int)directorRepository.count())+1) + "" + (new Random().nextInt(900)+100) + "" + LocalDateTime.now().getDayOfMonth() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getYear());	
			SchoolEntity school = schoolRepository.findByNumberSchool(schoolNumber);
			if (school == null) {return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Skola sa datim brojem ne postoji!"), HttpStatus.BAD_REQUEST);}
			DirectorEntity oldDirector = directorRepository.findBySchool(school);
			if (oldDirector != null) {
				AccountEntity oldAccount = oldDirector.getAccount();
				String username = oldDirector.getAccount().getUserName();
				oldDirector.setAccount(null);
				oldDirector.setDeletedAt(LocalDateTime.now());
				directorRepository.save(oldDirector);
				accountServ.delete(oldAccount);
				emailServ.deleteCredential(oldDirector.getEmail(), username);
			}
			AccountEntity accountE = accountServ.save(account);
			AddressEntity addressE = addressServ.save(address);
			DirectorEntity director = new DirectorEntity(directorDto.getFirstName(), directorDto.getLastName(), directorDto.getJmbg(), IGender. valueOf(directorDto.getGender()), accountE, addressE, directorDto.getEmail(), schoolUniqeNumber, school);
			director =	directorRepository.save(director);
			if(director != null) { 
				emailServ.sendCredential(director.getEmail(), userName, password, director.getIdUser(), "director");
			}
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllDirectors(Principal principal) {
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
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
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
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteDirectorById(@PathVariable Integer id) {
		try {
			DirectorEntity director = directorRepository.findById(id).get();
			AccountEntity account = director.getAccount();
			director.setDeletedAt(LocalDateTime.now());
			String username = director.getAccount().getUserName();
			director.setAccount(null);
			directorRepository.save(director);
			accountServ.delete(account);
			emailServ.deleteCredential(director.getEmail(), username);
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
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
	
	@RequestMapping(method = RequestMethod.PUT, value = "/forgottenCredential")
	public ResponseEntity<?> forgottenCredential(@Valid @RequestBody(required = false) EmailDTO emailDto, BindingResult result) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(emailDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		String password = emailDto.getEmail().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + emailDto.getEmail().substring(1, 2) + emailDto.getEmail().substring(0,1);
		String userName =  emailDto.getEmail().substring(0, emailDto.getEmail().indexOf('@')) + "Du";
		try {
			DirectorEntity director = directorRepository.findByEmail(emailDto.getEmail());
			director.getAccount().setPassword(password);
			director.getAccount().setUserName(userName);
			director = directorRepository.save(director);
			emailServ.sendGenerateCredential(director.getEmail(), userName, password, director.getIdUser(), "director");
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.PUT, value = "/changeCredential/{id}")
	public ResponseEntity<?> changeCredential(@Valid @RequestBody(required = false) AccountDTO accounDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(accounDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			DirectorEntity director = directorRepository.findById(id).get();
			director.getAccount().setPassword(accounDto.getPassword());
			director.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(director.getEmail(), accounDto.getUserName(), accounDto.getPassword(), director.getIdUser(), "director");
			return new ResponseEntity<DirectorEntity>(directorRepository.save(director), HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_USER", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/school")
	public ResponseEntity<?> getDirectorBySchool(@RequestParam(name = "idSchool") String id) {
		try {
			SchoolEntity school = schoolRepository.findById(Integer.parseInt(id)).orElse(null);
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Uneli ste nepostojecu skolu!"), HttpStatus.NOT_FOUND);
			}
			DirectorEntity director = directorRepository.findBySchool(school);
			if(director == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<DirectorEntity>(director, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_STUDENT", "ROLE_PARRENT", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/userinfo/{id}")
	public ResponseEntity<?> getUserInfoById(@PathVariable Integer id) {
		try {
			DirectorEntity user = directorRepository.findById(id).get();
			UserInfoDTO userInfo = new UserInfoDTO();
			userInfo.setSchoolNumber(Long.toString(user.getSchoolUniqeNumber()));
			userInfo.setFirstName(user.getFirstName());
			userInfo.setLastName(user.getLastName());
			userInfo.setEmail(user.getEmail());
			userInfo.setJmbg(user.getJmbg());
			userInfo.setGender(String.valueOf(user.getGender()));
			userInfo.setAddress(user.getAddress().getStreet().getNameStreet() + " " + user.getAddress().getHouseNumber().getHouseNumber() + ", " + user.getAddress().getCity().getNameCity() + ", " + user.getAddress().getCity().getBorough().getNumberBorough() + " " + user.getAddress().getCity().getBorough().getNameBorough() + ", " + user.getAddress().getCity().getBorough().getCountry());
			return new ResponseEntity<UserInfoDTO>(userInfo, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	 
}

package com.iktpreobuka.schooldiary.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.entities.dto.ParentDTO;
import com.iktpreobuka.schooldiary.entities.dto.UserInfoDTO;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.repositories.ParentRepository;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@RestController
@RequestMapping("/schoolDiary/users/parrent")
public class ParrentController {

	@Autowired
	private ParentRepository parentRepository;
	@Autowired
	private AddressService addressServ;
	@Autowired
	private EmailService emailServ;
	@Autowired
	private ErrorMessage errMsg;
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_PARRENT", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateParrentById(@Valid @RequestBody(required = false) ParentDTO parrentDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(parrentDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			ParentEntity parrent = parentRepository.findById(id).get();
			Integer addressId = parentRepository.findById(id).get().getAddress().getIdAddress();
			AddressEntity address = new AddressEntity(new StreetEntity(parrentDto.getParentNameStreet()), new HouseNumberEntity(parrentDto.getParentHouseNumber()), new CityEntity(parrentDto.getParentNameCity(), new BoroughEntity(parrentDto.getParentNameBorough(), parrentDto.getParentNumberBorough())));
			parrent.setFirstName(parrentDto.getParentFirstName());
			parrent.setLastName(parrentDto.getParentLastName());
			parrent.setJmbg(parrentDto.getParentJmbg());
			parrent.setGender(IGender.valueOf(parrentDto.getParentGender()));
			address = addressServ.update(addressId, address);
			parrent.setAddress(address);
			parrent = parentRepository.save(parrent);
			return new ResponseEntity<ParentEntity>(parrent, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllParrents() {
		try {
			Iterable<ParentEntity> parrents = parentRepository.findAll();
			if(!parrents.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<ParentEntity>>(parrents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getParrentById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<ParentEntity>(parentRepository.findById(id).get(), HttpStatus.OK);
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
			ParentEntity parrent = parentRepository.findByEmail(emailDto.getEmail());
			parrent.getAccount().setPassword(password);
			parrent.getAccount().setUserName(userName);
			parrent = parentRepository.save(parrent);
			emailServ.sendGenerateCredential(parrent.getEmail(), userName, password, parrent.getIdUser(), "director");
			return new ResponseEntity<ParentEntity>(parrent, HttpStatus.OK);
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
			ParentEntity parrent = parentRepository.findById(id).get();
			parrent.getAccount().setPassword(accounDto.getPassword());
			parrent.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(parrent.getEmail(), accounDto.getUserName(), accounDto.getPassword(), parrent.getIdUser(), "director");
			return new ResponseEntity<ParentEntity>(parentRepository.save(parrent), HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_STUDENT", "ROLE_PARRENT", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/userinfo/{id}")
	public ResponseEntity<?> getUserInfoById(@PathVariable Integer id) {
		try {
			ParentEntity user = parentRepository.findById(id).get();
			UserInfoDTO userInfo = new UserInfoDTO();
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
	
	@Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_DIRECTOR"})
	@RequestMapping(method = RequestMethod.GET, value = "/childSchool")
	public ResponseEntity<?> getParrentsByChildSchool(@RequestParam(name = "schoolNumber") Long number) {
		try {
			List<ParentEntity> parrents = parentRepository.findDistinctByStudentsSchoolNumberSchool(number);
			if (parrents.size() == 0) {
				return new ResponseEntity<RestError>(new RestError(410, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<ParentEntity>>(parrents, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

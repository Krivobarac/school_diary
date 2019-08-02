package com.iktpreobuka.schooldiary.controllers;

import java.util.NoSuchElementException;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.schooldiary.controllers.utils.RestError;
import com.iktpreobuka.schooldiary.entities.AccountEntity;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.SuperAdminEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.entities.dto.SuperAdminDTO;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.SuperAdminRepository;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;
import com.iktpreobuka.schooldiary.services.RoleService;
import com.iktpreobuka.schooldiary.services.UserService;

@JsonView(Views.SuperAdmin.class)
@Secured(value = {"ROLE_SUPER_ADMIN"})
@RestController
@RequestMapping("/schoolDiary/users/sa")
public class SuperAdminController {

	@Autowired
	private SuperAdminRepository superAdminRepository;
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
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewSuperAdmin(@Valid @RequestBody(required = false) SuperAdminDTO superAdminDto){
		if(superAdminDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_SUPER_ADMIN);
		String password = superAdminDto.getFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + superAdminDto.getFirstName().substring(1, 2) + superAdminDto.getLastName().substring(1,2);
		String userName =  superAdminDto.getEmail().substring(0, superAdminDto.getEmail().indexOf('@')) + "SA";		
		try {
			AddressEntity address = addressServ.save(new AddressEntity(new StreetEntity(superAdminDto.getNameStreet()), new HouseNumberEntity(superAdminDto.getHouseNumber()), new CityEntity(superAdminDto.getNameCity(), new BoroughEntity(superAdminDto.getNameBorough(), superAdminDto.getNumberBorough()))));
			AccountEntity account = accountServ.save(new AccountEntity(userName, new BCryptPasswordEncoder().encode(password), role));
			SuperAdminEntity superAdmin = superAdminRepository.save(new SuperAdminEntity(superAdminDto.getFirstName(), superAdminDto.getLastName(), superAdminDto.getJmbg(), IGender. valueOf(superAdminDto.getGender()), account, address, superAdminDto.getEmail()));
			if(superAdmin!= null) { 
				emailServ.sendCredential(superAdminDto.getEmail(), userName, password, superAdmin.getIdUser());
			}
			return new ResponseEntity<SuperAdminEntity>(superAdmin, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllSuperAdmins() {
		try {
			Iterable<SuperAdminEntity> superAdmins = superAdminRepository.findAll();
			if(!superAdmins.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<SuperAdminEntity>>(superAdmins, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getSuperAdminById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<SuperAdminEntity>(superAdminRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteSuperAdminById(@PathVariable Integer id) {
		try {
			SuperAdminEntity superAdmin = superAdminRepository.findById(id).get();
			superAdminRepository.delete(superAdmin);
			accountServ.delete(superAdmin.getAccount());
			addressServ.delete(superAdmin.getAddress());
			emailServ.deleteCredential(superAdmin.getEmail());
			return new ResponseEntity<SuperAdminEntity>(superAdmin, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateSuperAdminById(@Valid @RequestBody(required = false) SuperAdminDTO superAdminDto, @PathVariable Integer id) {
		if(superAdminDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			SuperAdminEntity superAdmin = superAdminRepository.findById(id).get();
			Integer addressId = superAdminRepository.findById(id).get().getAddress().getIdAddress();
			superAdmin.setFirstName(superAdminDto.getFirstName());
			superAdmin.setLastName(superAdminDto.getLastName());
			superAdmin.setJmbg(superAdminDto.getJmbg());;
			superAdmin.setGender(IGender.valueOf(superAdminDto.getGender()));
			superAdmin.setEmail(superAdminDto.getEmail());
			AddressEntity address = new AddressEntity(new StreetEntity(superAdminDto.getNameStreet()), new HouseNumberEntity(superAdminDto.getHouseNumber()), new CityEntity(superAdminDto.getNameCity(), new BoroughEntity(superAdminDto.getNameBorough(), superAdminDto.getNumberBorough())));
			address = addressServ.update(addressId, address);
			superAdmin.setAddress(address);
			return new ResponseEntity<SuperAdminEntity>(superAdminRepository.save(superAdmin), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/forgottenCredential")
	public ResponseEntity<?> forgottenCredential(@Valid @RequestBody(required = false) EmailDTO emailDto) {
		if(emailDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		String password = emailDto.getEmail().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + emailDto.getEmail().substring(1, 2) + emailDto.getEmail().substring(0,1);
		String userName =  emailDto.getEmail().substring(0, emailDto.getEmail().indexOf('@')) + "SA";
		try {
			SuperAdminEntity superAdmin = superAdminRepository.findByEmail(emailDto.getEmail());
			UserEntity user = userServ.getById(superAdmin);
			user.getAccount().setUserName(userName);
			user.getAccount().setPassword(password);	
			accountServ.updateById(user.getAccount().getIdAccount(), user.getAccount());
			emailServ.sendGenerateCredential(superAdmin.getEmail(), userName, password, superAdmin.getIdUser());
			return new ResponseEntity<SuperAdminEntity>(superAdmin, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/changeCredential/{id}")
	public ResponseEntity<?> changeCredential(@RequestBody(required = false) AccountDTO accounDto, @PathVariable Integer id) {
		if(accounDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			SuperAdminEntity superAdmin = superAdminRepository.findById(id).get();
			superAdmin.getAccount().setPassword(accounDto.getPassword());
			superAdmin.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(superAdmin.getEmail(), accounDto.getUserName(), accounDto.getPassword(), superAdmin.getIdUser());
			return new ResponseEntity<SuperAdminEntity>(superAdminRepository.save(superAdmin), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}

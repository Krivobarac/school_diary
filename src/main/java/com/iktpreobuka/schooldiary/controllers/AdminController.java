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
import com.iktpreobuka.schooldiary.entities.AdminEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.AdminEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.SuperAdminEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.entities.dto.AccountDTO;
import com.iktpreobuka.schooldiary.entities.dto.AdminDTO;
import com.iktpreobuka.schooldiary.entities.dto.DirectorDTO;
import com.iktpreobuka.schooldiary.entities.dto.EmailDTO;
import com.iktpreobuka.schooldiary.entities.dto.SuperAdminDTO;
import com.iktpreobuka.schooldiary.enums.IGender;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.AdminRepository;
import com.iktpreobuka.schooldiary.repositories.SchoolRepository;
import com.iktpreobuka.schooldiary.repositories.SuperAdminRepository;
import com.iktpreobuka.schooldiary.securities.Views;
import com.iktpreobuka.schooldiary.services.AccountService;
import com.iktpreobuka.schooldiary.services.AddressService;
import com.iktpreobuka.schooldiary.services.EmailService;
import com.iktpreobuka.schooldiary.services.RoleService;
import com.iktpreobuka.schooldiary.services.UserService;

@RestController
@RequestMapping("/schoolDiary/users/admin")
public class AdminController {
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private SchoolRepository schoolRepository;
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
	private ErrorMessage errMsg;
	
	@Secured(value = {"ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody(required = false) AdminDTO adminDto, BindingResult result){
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(adminDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		RoleEntity role = roleServ.getRoleByRole(IRole.ROLE_ADMIN);
		String password = adminDto.getFirstName().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + adminDto.getFirstName().substring(1, 2) + adminDto.getLastName().substring(1,2);
		String userName =  adminDto.getEmail().substring(0, adminDto.getEmail().indexOf('@')) + "A";
		long schoolNumber = adminDto.getSchoolNumber();
		AccountEntity account = new AccountEntity(userName, new BCryptPasswordEncoder().encode(password), role);
		AddressEntity address = new AddressEntity(new StreetEntity(adminDto.getNameStreet()), new HouseNumberEntity(adminDto.getHouseNumber()), new CityEntity(adminDto.getNameCity(), new BoroughEntity(adminDto.getNameBorough(), adminDto.getNumberBorough())));
		try {
			Long schoolUniqeNumber = Long.parseLong((((int)adminRepository.count())+1) + "" + (new Random().nextInt(900)+100) + "" + LocalDateTime.now().getDayOfMonth() + "" + LocalDateTime.now().getMonthValue() + "" + LocalDateTime.now().getYear());	
			SchoolEntity school = schoolRepository.findByNumberSchool(schoolNumber);
			if (school == null) {return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Skola sa datim brojem ne postoji!"), HttpStatus.BAD_REQUEST);}
			AdminEntity oldAdmin = adminRepository.findBySchool(school);
			if (oldAdmin != null) {
				AccountEntity oldAccount = oldAdmin.getAccount();
				String username = oldAdmin.getAccount().getUserName();
				oldAdmin.setAccount(null);
				oldAdmin.setDeletedAt(LocalDateTime.now());
				adminRepository.save(oldAdmin);
				accountServ.delete(oldAccount);
				emailServ.deleteCredential(oldAdmin.getEmail(), username);
			}
			AccountEntity accountE = accountServ.save(account);
			AddressEntity addressE = addressServ.save(address);
			AdminEntity admin = new AdminEntity(adminDto.getFirstName(), adminDto.getLastName(), adminDto.getJmbg(), IGender. valueOf(adminDto.getGender()), accountE, addressE, adminDto.getEmail(), schoolUniqeNumber, school);
			admin =	adminRepository.save(admin);
			if(admin != null) { 
				emailServ.sendCredential(admin.getEmail(), userName, password, admin.getIdUser(), "admin");
			}
			return new ResponseEntity<AdminEntity>(admin, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<RestError>(new RestError(550, "Exception occurred: Korisnik sa ovom rolom postoji"), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllAdmins() {
		try {
			Iterable<AdminEntity> admins = adminRepository.findAll();
			if(!admins.iterator().hasNext()) {
				return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Iterable<AdminEntity>>(admins, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAdminById(@PathVariable Integer id) {
		try {
			return new ResponseEntity<AdminEntity>(adminRepository.findById(id).get(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteAdminById(@PathVariable Integer id) {
		try {
			AdminEntity admin = adminRepository.findById(id).get();
			AccountEntity account = admin.getAccount();
			admin.setDeletedAt(LocalDateTime.now());
			String username = admin.getAccount().getUserName();
			admin.setAccount(null);
			adminRepository.save(admin);
			accountServ.delete(account);
			emailServ.deleteCredential(admin.getEmail(), username);
			return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
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
	public ResponseEntity<?> updateDirectorById(@Valid @RequestBody(required = false) AdminDTO adminDto, BindingResult result, @PathVariable Integer id) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(adminDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		try {
			AdminEntity admin = adminRepository.findById(id).get();
			Integer addressId = adminRepository.findById(id).get().getAddress().getIdAddress();
			AddressEntity address = new AddressEntity(new StreetEntity(adminDto.getNameStreet()), new HouseNumberEntity(adminDto.getHouseNumber()), new CityEntity(adminDto.getNameCity(), new BoroughEntity(adminDto.getNameBorough(), adminDto.getNumberBorough())));
			admin.setFirstName(adminDto.getFirstName());
			admin.setLastName(adminDto.getLastName());
			admin.setJmbg(adminDto.getJmbg());;
			admin.setGender(IGender.valueOf(adminDto.getGender()));
			admin.setEmail(adminDto.getEmail());
			address = addressServ.update(addressId, address);
			admin.setAddress(address);
			admin = adminRepository.save(admin);
			return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<RestError>(new RestError(501, "Morate uneti brojcanu vrednost: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<RestError>(new RestError(404, "Nema rezultata"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured(value = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.PUT, value = "/forgottenCredential")
	public ResponseEntity<?> forgottenCredential(@Valid @RequestBody(required = false) EmailDTO emailDto, BindingResult result) {
		if(result.hasErrors()) {return new ResponseEntity<>(errMsg.createErrorMessage(result), HttpStatus.BAD_REQUEST);}
		if(emailDto == null) { return new ResponseEntity<RestError>(new RestError(450, "Exception occurred: " + new Exception().getMessage()), HttpStatus.BAD_REQUEST);}
		String password = emailDto.getEmail().substring(0, 1).toUpperCase() + (new Random().nextInt(900)+100) + "@" + emailDto.getEmail().substring(1, 2) + emailDto.getEmail().substring(0,1);
		String userName =  emailDto.getEmail().substring(0, emailDto.getEmail().indexOf('@')) + "SA";
		try {
			AdminEntity admin = adminRepository.findByEmail(emailDto.getEmail());
			UserEntity user = userServ.getById(admin);
			user.getAccount().setUserName(userName);
			user.getAccount().setPassword(password);	
			accountServ.updateById(user.getAccount().getIdAccount(), user.getAccount());
			emailServ.sendGenerateCredential(admin.getEmail(), userName, password, admin.getIdUser(), "admin");
			return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
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
			AdminEntity admin = adminRepository.findById(id).get();
			admin.getAccount().setPassword(accounDto.getPassword());
			admin.getAccount().setUserName(accounDto.getUserName());
			emailServ.changeCredential(admin.getEmail(), accounDto.getUserName(), accounDto.getPassword(), admin.getIdUser(), "admin");
			return new ResponseEntity<AdminEntity>(adminRepository.save(admin), HttpStatus.OK);
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
	public ResponseEntity<?> getAdminBySchool(@RequestParam(name = "idSchool") String id) {
		try {
			SchoolEntity school = schoolRepository.findById(Integer.parseInt(id)).orElse(null);
			if (school == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Uneli ste nepostojecu skolu!"), HttpStatus.NOT_FOUND);
			}
			AdminEntity admin = adminRepository.findBySchool(school);
			if(admin == null) {
				return new ResponseEntity<RestError>(new RestError(405, "Nema rezultata!"), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestError>(new RestError(500, "Exception occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}

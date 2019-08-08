package com.iktpreobuka.schooldiary.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	JavaMailSender mailSender;
	
	@Override
	public void sendGenerateCredential(String email, String username, String password, Integer id, String role) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Novi Kredencijali");
		String text = "<html><body><h1 style ='color: blue'>Vase novo korisnicko ime i sifra</h1>\n\r"
				+ "<p>Korisnicko ime: " + username + "</p>\n\r"
				+ "<p>Sifra: " + password + "</p>\n\r"
				+ "<p>Radi vase bezbednosti izvrsite izmenu kredencijala podacima koji su vama poznati preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/users/" + role + "/changeCredential' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/" + role + "/changeCredential/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void sendCredential(String email, String username, String password, Integer id, String role) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Korisnicki Kredencijali");
		String text = "<html><body><h1 style ='color: green'>Izvrsena je vasa registracija na <a href='http://localhost:8080/schoolDiaryusers/sa/" + id + "' style='text-decoration: none; color: blue'>Elektronski Dnevnik</a></h1>\n\r"
				+ "<p>Korisnicko ime: " + username + "</p>\n\r"
				+ "<p>Sifra: " + password + "</p>\n\r"
				+ "<p>Radi vase bezbednosti izvrsite izmenu kredencijala podacima koji su vama poznati preko sledeceg linka</p>\n\r"
				+ ""
				+ "<a href='http://localhost:8080/schoolDiary/users/" + role + "/changeCredential" + id + "' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/" + role + "/changeCredential/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void changeCredential(String email, String username, String password, Integer id, String role) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Korisnicki Kredencijali");
		String text = "<html><body><h1 style ='color: green'>Uspesno ste izmenili vase kredencijale</h1>\n\r"
				+ "<p>Korisnicko ime: " + username + "</p>\n\r"
				+ "<p>Sifra: " + password + "</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/users/" + role + "/" + id + "' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/" + role + "/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void deleteCredential(String email, String username) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Brisanje iz sistema");
		String text = "<html><body><h1 style ='color: red'>Korisnik " + username + " je izbrisan iz sistema elektonskog dnevnika</h1>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/' style='text-decoration: none;'>http://localhost:8080/schoolDiary/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void sendStudentsAndParentsCredential(String email, String usernameP, String usernameS, String passwordP, String passwordS, Integer idP, Integer idS, String roleP, String roleS) throws Exception{
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Korisnicki Kredencijali");
		String text = "<html><body><h1 style ='color: green'>Izvrsena je registracija vaseg deteta na <a href='http://localhost:8080/schoolDiaryusers/" + roleP + "/" + idP + "' style='text-decoration: none; color: blue'>Elektronski Dnevnik</a></h1>\n\r"
				+ "<p>Korisnicko ime za roditelja: " + usernameP + "</p>\n\r"
				+ "<p>Sifra za roditelja: " + passwordP + "</p>\n\r"
				+ "<p>Radi vase bezbednosti izvrsite izmenu kredencijala podacima koji su vama poznati preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/users/" + roleP + "/changeCredential/" + idP + "' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/" + roleP + "/changeCredential/</a>\n\r"
				+ "<p>Korisnicko ime za ucenika: " + usernameS + "</p>\n\r"
				+ "<p>Sifra za ucenika: " + passwordS + "</p>\n\r"
				+ "<p>Ucenik moze izvrsiti izmenu kredencijala preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/users/" + roleS + "/changeCredential/" + idS + "' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/" + roleS + "/changeCredential/</a>\n\r"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	public void sendMark(String email, Integer id) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Ocena");
		String text = "<html><body><h1 style ='color: green'>Izvrseno je ocenjivanje vaseg deteta</h1>\n\r"
				+ "<p>Ocenu mozete pogledati preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/evaluation/student/" + id + "'>http://localhost:8080/schoolDiary/evaluation/student/" + id + "</a>\n\r"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	public void sendUpdatedMark(String email, Integer id) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Ocena");
		String text = "<html><body><h1 style ='color: green'>Izvrsena je izmena ocene</h1>\n\r"
				+ "<p>Ocenu mozete pogledati preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/evaluation/student/" + id + "'>http://localhost:8080/schoolDiary/evaluation/student/" + id + "</a>\n\r"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}

	@Override
	public void sendDeletedMark(String email, Integer id) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Ocena");
		String text = "<html><body><h1 style ='color: green'>Izbrisana je ocena</h1>\n\r"
				+ "<p>Promene mozete pogledati preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/evaluation/student/" + id + "'>http://localhost:8080/schoolDiary/evaluation/student/" + id + "</a>\n\r"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
		
	}

}

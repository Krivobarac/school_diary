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
	public void sendGenerateCredential(String email, String username, String password, Integer id) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Novi Kredencijali");
		String text = "<html><body><h1 style ='color: blue'>Vase novo korisnicko ime i sifra</h1>\n\r"
				+ "<p>Korisnicko ime: " + username + "</p>\n\r"
				+ "<p>Sifra: " + password + "</p>\n\r"
				+ "<p>Radi vase bezbednosti izvrsite izmenu kredencijala podacima koji su vama poznati preko sledeceg linka</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/users/sa/changeCredential' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/sa/changeCredential/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void sendCredential(String email, String username, String password, Integer id) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Korisnicki Kredencijali");
		String text = "<html><body><h1 style ='color: green'>Izvrsena je vasa registracija na <a href='http://localhost:8080/schoolDiaryusers/sa/" + id + "' style='text-decoration: none; color: blue'>Elektronski Dnevnik</a></h1>\n\r"
				+ "<p>Korisnicko ime: " + username + "</p>\n\r"
				+ "<p>Sifra: " + password + "</p>\n\r"
				+ "<p>Radi vase bezbednosti izvrsite izmenu kredencijala podacima koji su vama poznati preko sledeceg linka</p>\n\r"
				+ ""
				+ "<a href='http://localhost:8080/schoolDiary/users/sa/changeCredential" + id + "' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/sa/changeCredential/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void changeCredential(String email, String username, String password, Integer id) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Korisnicki Kredencijali");
		String text = "<html><body><h1 style ='color: green'>Uspesno ste izmenili vase kredencijale</h1>\n\r"
				+ "<p>Korisnicko ime: " + username + "</p>\n\r"
				+ "<p>Sifra: " + password + "</p>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/users/sa/" + id + "' style='text-decoration: none;'>http://localhost:8080/schoolDiary/users/sa/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}
	
	@Override
	public void deleteCredential(String email) throws Exception {
		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(email);
		helper.setSubject("Brisanje iz sistema");
		String text = "<html><body><h1 style ='color: red'>Izbrisani ste iz sistema elektonskog dnevnika</h1>\n\r"
				+ "<a href='http://localhost:8080/schoolDiary/' style='text-decoration: none;'>http://localhost:8080/schoolDiary/</a>"
				+ "</body></html>";
		helper.setText(text, true);
		mailSender.send(mail);
	}

}

package com.iktpreobuka.schooldiary.services;

public interface EmailService {
	void sendGenerateCredential(String email, String username, String password, Integer id) throws Exception;
	void sendCredential(String email, String username, String password, Integer id) throws Exception;
	void deleteCredential(String email) throws Exception;
	void changeCredential(String email, String username, String password, Integer id) throws Exception;
}

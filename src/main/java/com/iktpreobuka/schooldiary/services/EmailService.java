package com.iktpreobuka.schooldiary.services;

public interface EmailService {
	void sendGenerateCredential(String email, String username, String password, Integer id, String role) throws Exception;
	void sendCredential(String email, String username, String password, Integer id, String role) throws Exception;
	void deleteCredential(String email, String Username) throws Exception;
	void changeCredential(String email, String username, String password, Integer id, String role) throws Exception;
	public void sendStudentsAndParentsCredential(String email, String usernameP, String usernameS, String passwordP, String passwordS, Integer idP, Integer idS, String roleP, String roleS) throws Exception;
	public void sendMark(String email, Integer id) throws Exception;
	public void sendUpdatedMark(String email, Integer id) throws Exception;
	public void sendDeletedMark(String email, Integer id) throws Exception;
}

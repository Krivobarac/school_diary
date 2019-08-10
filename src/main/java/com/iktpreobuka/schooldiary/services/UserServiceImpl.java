package com.iktpreobuka.schooldiary.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.repositories.UserRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	public UserEntity save(UserEntity ue) {
		UserEntity user;
		try {
			user = userRepository.save(ue);
		} catch (Exception e) {
			user = userRepository.findByJmbg(ue.getJmbg());
		} 
		return user;
	}
	
	public UserEntity delete(UserEntity ue) {
		UserEntity user = userRepository.findById(ue.getIdUser()).orElse(null);
		if(user != null) {
			userRepository.delete(user);
		}
		return user;
	}
	
	public UserEntity getById(UserEntity ue) {
		return userRepository.findById(ue.getIdUser()).orElse(null);
	}
	
	public List<UserEntity> getAll() {
		return (List<UserEntity>) userRepository.findAll();
	}

	public UserEntity update(Integer id, UserEntity ue) {
		UserEntity user;
		try {
			user = userRepository.save(ue);
		} catch (Exception e) {
			user = userRepository.findById(id).get();
		} 
		return user;
	}

	public UserEntity findbyAccount(Integer id) {
		return userRepository.findByAccountIdAccount(id);
	}
}

package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.UserEntity;

public interface UserService {
	public UserEntity save(UserEntity ue);
	
	public UserEntity delete(UserEntity ue);
	
	public UserEntity getById(UserEntity ue);
	
	public List<UserEntity> getAll();

	public UserEntity update(Integer id, UserEntity ue);
}

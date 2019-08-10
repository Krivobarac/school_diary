package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.AccountEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;

public interface AccountService {
	public AccountEntity save(AccountEntity ae);
	
	public AccountEntity delete(AccountEntity ae);
	
	public AccountEntity getById(Integer id);
	
	public List<AccountEntity> getAll();

	public AccountEntity updateById(Integer id, AccountEntity ae);
	
	public AccountEntity getAccountByUser(UserEntity user);
}

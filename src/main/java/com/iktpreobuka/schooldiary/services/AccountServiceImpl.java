package com.iktpreobuka.schooldiary.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.entities.AccountEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;
import com.iktpreobuka.schooldiary.repositories.AccountRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@Service
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AccountRepository accountRepository;
	
	public AccountEntity save(AccountEntity ae) {
		AccountEntity account = accountRepository.findByPasswordAndUserName(ae.getPassword(), ae.getUserName());
		if (account == null) {
			account = accountRepository.save(ae);
		}
		return account;
	}
	
	public AccountEntity delete(AccountEntity ae) {
		AccountEntity account = accountRepository.findById(ae.getIdAccount()).orElse(null);
		if(account != null) {
			accountRepository.delete(account);
		}
		return account;
	}
	
	public AccountEntity getById(Integer id) {
		return accountRepository.findById(id).get();
	}
	
	public List<AccountEntity> getAll() {
		return (List<AccountEntity>) accountRepository.findAll();
	}

	public AccountEntity updateById(Integer id, AccountEntity ae) {
		AccountEntity account = accountRepository.findById(id).orElse(null);
		if(account != null) {
			account.setPassword(ae.getPassword());
			account.setUserName(ae.getUserName());
			accountRepository.save(account);
		}
		return account;
	}
	
	public AccountEntity getAccountByUser(UserEntity user) {
		return accountRepository.findByUsers(user);
	}

}

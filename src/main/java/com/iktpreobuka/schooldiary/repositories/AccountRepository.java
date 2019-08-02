package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AccountEntity;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {
	AccountEntity findByPasswordAndUserName(String password, String username);
}

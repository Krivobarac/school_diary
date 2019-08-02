package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	UserEntity findByJmbg(String jmbg);
	List<UserEntity> findByAddress(AddressEntity address);
}

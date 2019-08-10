package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.BoroughEntity;

@Repository
public interface BoroughRepository extends CrudRepository<BoroughEntity, Integer> {
	BoroughEntity findByNameBoroughAndNumberBorough(String boroughName, String numberBorough);
	BoroughEntity findByNameBorough(String name);
}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.StreetEntity;

@Repository
public interface StreetRepository extends CrudRepository<StreetEntity, Integer>{
	StreetEntity findByNameStreet(String nameStreet);
}

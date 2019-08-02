package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.StreetEntity;

@Repository
public interface StreetRepository extends CrudRepository<StreetEntity, Integer>{
	StreetEntity findByNameStreet(String nameStreet);
}

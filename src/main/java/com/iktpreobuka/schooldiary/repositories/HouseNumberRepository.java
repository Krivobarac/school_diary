package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;

@Repository
public interface HouseNumberRepository extends CrudRepository<HouseNumberEntity, Integer> {
	HouseNumberEntity findByHouseNumber(String houseNumber);
}

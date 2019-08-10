package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Integer> {
	CityEntity findByNameCityAndBorough(String nameCity, BoroughEntity borough);
	CityEntity findByNameCity(String nameCity);
	List<CityEntity> findByBorough(BoroughEntity borough);
}

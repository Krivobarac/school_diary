package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.entities.StreetEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Integer> {
	AddressEntity findByStreetAndCityAndHouseNumber(StreetEntity street, CityEntity city, HouseNumberEntity houseNumber);
	List<AddressEntity> findByCity(CityEntity city);
	List<AddressEntity> findByStreet(StreetEntity street);
	List<AddressEntity> findByHouseNumber(HouseNumberEntity hoseNumber);
}

package com.iktpreobuka.schooldiary.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.repositories.HouseNumberRepository;

@Service
public class HouseNumberServiceImpl implements HouseNumberService{
	@Autowired
	HouseNumberRepository houseNumberRepository;
	
	public HouseNumberEntity save(HouseNumberEntity hne) {
		HouseNumberEntity houseNumber;
		try {
			houseNumber = houseNumberRepository.save(hne);
		} catch (Exception e) {
			houseNumber = houseNumberRepository.findByHouseNumber(hne.getHouseNumber());
		}
		return houseNumber;
	}
	
	public HouseNumberEntity delete(HouseNumberEntity hne) {
		HouseNumberEntity houseNumber = houseNumberRepository.findById(hne.getIdNumber()).orElse(null);
		if (houseNumber != null) {
			houseNumberRepository.delete(houseNumber);
		}
		return houseNumber;
	}
	
	public HouseNumberEntity getById(Integer id) {
		return houseNumberRepository.findById(id).get();
	}
	
	public List<HouseNumberEntity> getAll() {
		return (List<HouseNumberEntity>) houseNumberRepository.findAll();
	}

	public void deleteById(Integer id) {
		houseNumberRepository.deleteById(id);
	}

	public HouseNumberEntity update(Integer id, HouseNumberEntity hne) {
		HouseNumberEntity houseNumber;
		try {
			houseNumber = houseNumberRepository.save(hne);
		} catch (Exception e) {
			houseNumber = houseNumberRepository.findById(id).get();
		}
		return houseNumber;
	}

}

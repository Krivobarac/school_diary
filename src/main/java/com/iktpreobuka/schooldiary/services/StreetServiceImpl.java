package com.iktpreobuka.schooldiary.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.repositories.StreetRepository;

@Service
@JsonSerialize
public class StreetServiceImpl implements StreetService {
	@Autowired
	private StreetRepository streetRepository;
	
	public StreetEntity save(StreetEntity se) {
		StreetEntity street;
		try {
			street = streetRepository.save(se);
		} catch (Exception e) {
			street = streetRepository.findByNameStreet(se.getNameStreet());
		}
		return street;
	}
	
	public StreetEntity delete(StreetEntity se) {
		StreetEntity street = streetRepository.findById(se.getIdStreet()).orElse(null);
		if(street != null) {
			streetRepository.delete(street);
		}
		return street;
	}
	
	public StreetEntity getById(Integer id) {
		return streetRepository.findById(id).orElse(null);
	}
	
	public List<StreetEntity> getAll() {
		return (List<StreetEntity>) streetRepository.findAll();
	}

	@Override
	public StreetEntity update(Integer id, StreetEntity se) {
		StreetEntity street;
		try {
			street = streetRepository.save(se);
		} catch (Exception e) {
			street = streetRepository.findById(id).get();
		}
		return street;
	}

}

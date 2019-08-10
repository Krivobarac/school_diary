package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;

public interface HouseNumberService {
	public HouseNumberEntity save(HouseNumberEntity hne);
	
	public HouseNumberEntity delete(HouseNumberEntity hne);
	
	public HouseNumberEntity getById(Integer id);
	
	public HouseNumberEntity update(Integer id, HouseNumberEntity hne);
	
	public void deleteById(Integer id);
	
	public List<HouseNumberEntity> getAll();
}

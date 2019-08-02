package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;

public interface CityService {
	public CityEntity save(CityEntity ce);
	
	public CityEntity delete(CityEntity ce);
	
	public void deleteById(Integer id);
	
	public CityEntity getById(Integer id);
	
	public List<CityEntity> getAll();
	
	public CityEntity update(Integer id, CityEntity ae);
	
	public List<CityEntity> findByBorough(BoroughEntity be);
}

package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.StreetEntity;

public interface StreetService {
	public StreetEntity save(StreetEntity se);
	
	public StreetEntity delete(StreetEntity se);
	
	public StreetEntity getById(Integer id);
	
	public StreetEntity update(Integer id, StreetEntity se);
	
	public List<StreetEntity> getAll();
}

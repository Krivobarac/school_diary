package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.BoroughEntity;

public interface BoroughService {
	public BoroughEntity save(BoroughEntity be);
	
	public BoroughEntity delete(BoroughEntity be);
	
	public void deleteById(Integer id);
	
	public BoroughEntity getById(Integer id);
	
	public BoroughEntity update(Integer id, BoroughEntity be);
	
	public List<BoroughEntity> getAll();
}

package com.iktpreobuka.schooldiary.services;

import java.util.List;

import com.iktpreobuka.schooldiary.entities.AddressEntity;

public interface AddressService {
	public AddressEntity save(AddressEntity ae);
	
	public void delete(AddressEntity ae);
	
	public AddressEntity getById(Integer ae);
	
	public List<AddressEntity> getAll();
	
	public AddressEntity update(Integer id, AddressEntity ae);
	
}

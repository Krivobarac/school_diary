package com.iktpreobuka.schooldiary.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.repositories.BoroughRepository;

@Service
public class BoroughServiceImpl implements BoroughService{
	@Autowired
	private BoroughRepository boroughRepository;

	public BoroughEntity save(BoroughEntity be) {
		BoroughEntity borough;
		try {
			borough = boroughRepository.save(be);
		} catch (Exception e) {
			borough = boroughRepository.findByNameBoroughAndNumberBorough(be.getNameBorough(), be.getNumberBorough());
		} 
		return borough;
	}
	
	public BoroughEntity delete(BoroughEntity be) {
		BoroughEntity borough = boroughRepository.findById(be.getIdBorough()).orElse(null);
		if(borough != null) {
			boroughRepository.delete(be);
		}
		return borough;
	}
	
	public void deleteById(Integer id) {
		boroughRepository.deleteById(id);
	}
	
	public BoroughEntity getById(Integer id) {
		return boroughRepository.findById(id).get();
	}
	
	public List<BoroughEntity> getAll() {
		return (List<BoroughEntity>) boroughRepository.findAll();
	}

	@Override
	public BoroughEntity update(Integer id, BoroughEntity be) {
		BoroughEntity borough;
		try {
			borough = boroughRepository.save(be);
		} catch (Exception e) {
			borough = boroughRepository.findById(id).get();
		} 
		return borough;
	}

}

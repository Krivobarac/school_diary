package com.iktpreobuka.schooldiary.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.repositories.CityRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@Service
public class CityServiceImpl implements CityService {
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private BoroughService boroughServ;
	
	public CityEntity save(CityEntity ce) {
		CityEntity city;
		try {
			city = cityRepository.save(ce);
		} catch (Exception e) {
			city = cityRepository.findByNameCity(ce.getNameCity());
		}
		return city;
	}
	
	public CityEntity delete(CityEntity ce) {
		CityEntity city = cityRepository.findById(ce.getIdCity()).orElse(null);
		if (city != null) {
			cityRepository.delete(city);
		}
		try {
			boroughServ.delete(ce.getBorough());
		} catch (Exception e) {
		} 
		return city;
	}
	
	public CityEntity getById(Integer id) {
		return cityRepository.findById(id).get();
	}
	
	public List<CityEntity> getAll() {
		return (List<CityEntity>) cityRepository.findAll();
	}
	
	public List<CityEntity> findByBorough(BoroughEntity be) {
		return cityRepository.findByBorough(be);
	}

	public void deleteById(Integer id) {
		cityRepository.deleteById(id);
	}	
	
	public CityEntity update(Integer id, CityEntity ce) {
		CityEntity city;
		try {
			city = cityRepository.save(ce);
		} catch (Exception e) {
			city = cityRepository.findById(id).get();
		}
		return city;
	}
}

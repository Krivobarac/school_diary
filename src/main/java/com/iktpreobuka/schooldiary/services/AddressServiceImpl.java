package com.iktpreobuka.schooldiary.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.repositories.AddressRepository;
import com.iktpreobuka.schooldiary.repositories.UserRepository;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private StreetService streetServ;
	@Autowired
	private BoroughService boroughServ;
	@Autowired
	private HouseNumberService houseNumberServ;
	@Autowired
	private CityService cityServ;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;
	
	public AddressEntity save(AddressEntity ae) {
		BoroughEntity borough = boroughServ.save(new BoroughEntity(ae.getCity().getBorough().getNameBorough(), ae.getCity().getBorough().getNumberBorough()));
		CityEntity city = cityServ.save(new CityEntity(ae.getCity().getNameCity(), borough));
		StreetEntity street = streetServ.save(new StreetEntity(ae.getStreet().getNameStreet()));
		HouseNumberEntity houseNumber = houseNumberServ.save(new HouseNumberEntity(ae.getHouseNumber().getHouseNumber()));
		AddressEntity address = addressRepository.findByStreetAndCityAndHouseNumber(street, city, houseNumber);
		if(address == null) {
			address = addressRepository.save(new AddressEntity(street, houseNumber, city));
		}
		return address;
	}
	
	public void delete(AddressEntity ae) {
		AddressEntity address = addressRepository.findById(ae.getIdAddress()).orElse(null);
		if(address != null) {
			Integer users = userRepository.findByAddress(address).size();
			if (users < 1) {
				addressRepository.delete(address);
				cityServ.delete(address.getCity());
				streetServ.delete(address.getStreet());
				houseNumberServ.delete(address.getHouseNumber());
			}
		}
	}
	
	@Transactional
	public AddressEntity getById(Integer ae) {
		return addressRepository.findById(ae).orElse(null);
	}
	
	public List<AddressEntity> getAll() {
		return (List<AddressEntity>) addressRepository.findAll();
	}
	
	public AddressEntity update(Integer id, AddressEntity ae) {
		AddressEntity address = null;
		address = addressRepository.findById(id).get();
		
		Integer boroughs = cityServ.findByBorough(address.getCity().getBorough()).size();
		Integer cities = addressRepository.findByCity(address.getCity()).size();
		Integer streets = addressRepository.findByStreet(address.getStreet()).size();
		Integer houseNumbers = addressRepository.findByHouseNumber(address.getHouseNumber()).size();
		
		CityEntity city;
		StreetEntity street;
		HouseNumberEntity houseNumber;
		BoroughEntity borough;
		
		if (boroughs > 1) {
			borough = boroughServ.save(new BoroughEntity(ae.getCity().getBorough().getNameBorough(), ae.getCity().getBorough().getNumberBorough()));
		} else {
			address.getCity().getBorough().setNameBorough(ae.getCity().getBorough().getNameBorough());
			address.getCity().getBorough().setNumberBorough(ae.getCity().getBorough().getNumberBorough());
			borough = boroughServ.update(address.getCity().getBorough().getIdBorough(), address.getCity().getBorough());
		}
		if (cities > 1) {
			city = cityServ.save(new CityEntity(ae.getCity().getNameCity(), borough));
		} else {
			address.getCity().setNameCity(ae.getCity().getNameCity());
			city = cityServ.update(address.getCity().getIdCity(), address.getCity());
		}
		if (streets > 1) {
			street = streetServ.save(new StreetEntity(ae.getStreet().getNameStreet()));
		} else {
			address.getStreet().setNameStreet(ae.getStreet().getNameStreet());
			street = streetServ.update(address.getStreet().getIdStreet(), address.getStreet());
		}
		if (houseNumbers > 1) {
			houseNumber = houseNumberServ.save(new HouseNumberEntity(ae.getHouseNumber().getHouseNumber()));
		} else {
			address.getHouseNumber().setHouseNumber(ae.getHouseNumber().getHouseNumber());
			houseNumber = houseNumberServ.update(address.getHouseNumber().getIdNumber(), address.getHouseNumber());
		}
		address.setCity(city);
		address.setHouseNumber(houseNumber);
		address.setStreet(street);
		try {
			address = addressRepository.save(address);
		} catch (Exception e) {
			address = addressRepository.findByStreetAndCityAndHouseNumber(street, city, houseNumber);
		}
		return address;
		
		
	}
}

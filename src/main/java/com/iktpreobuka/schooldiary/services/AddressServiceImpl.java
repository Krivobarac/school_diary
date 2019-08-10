package com.iktpreobuka.schooldiary.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.schooldiary.entities.StreetEntity;
import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.entities.AddressEntity;
import com.iktpreobuka.schooldiary.entities.BoroughEntity;
import com.iktpreobuka.schooldiary.entities.CityEntity;
import com.iktpreobuka.schooldiary.entities.HouseNumberEntity;
import com.iktpreobuka.schooldiary.repositories.AddressRepository;
import com.iktpreobuka.schooldiary.repositories.UserRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
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
			address = new AddressEntity(street, houseNumber, city);
			if (address.getIdAddress() == null) {
				address = addressRepository.save(address);
			}
		}	
		return address;
	}
	
	public AddressEntity delete(AddressEntity ae) {
		AddressEntity address = addressRepository.findById(ae.getIdAddress()).orElse(null);
		
		if(address != null) {
			Integer boroughs = cityServ.findByBorough(address.getCity().getBorough()).size();
			Integer cities = addressRepository.findByCity(address.getCity()).size();
			Integer streets = addressRepository.findByStreet(address.getStreet()).size();
			Integer houseNumbers = addressRepository.findByHouseNumber(address.getHouseNumber()).size();
			
			CityEntity city = cityServ.getById(address.getCity().getIdCity());
			StreetEntity street = streetServ.getById(address.getStreet().getIdStreet());
			HouseNumberEntity houseNumber = houseNumberServ.getById(address.getHouseNumber().getIdNumber());
			BoroughEntity borough = boroughServ.getById(address.getCity().getBorough().getIdBorough());
			
			if (boroughs > 1) {
				borough = boroughServ.delete(borough);
			}
			if (cities > 1) {
				city = cityServ.delete(city);
			}
			if (streets > 1) {
				street = streetServ.delete(street);
			}
			if (houseNumbers > 1) {
				houseNumber = houseNumberServ.delete(houseNumber);
			}
			Integer users = userRepository.findByAddress(address).size();
			if (users > 1) {
				addressRepository.delete(address);
			}
		}
		return address;
	}
	
	public AddressEntity getById(Integer ae) {
		return addressRepository.findById(ae).orElse(null);
	}
	
	public List<AddressEntity> getAll() {
		return (List<AddressEntity>) addressRepository.findAll();
	}
	
	public AddressEntity update(Integer id, AddressEntity ae) {
		AddressEntity address = addressRepository.findById(id).get();
		
		
		Integer boroughs = cityServ.findByBorough(address.getCity().getBorough()).size();
		Integer cities = addressRepository.findByCity(address.getCity()).size();
		Integer streets = addressRepository.findByStreet(address.getStreet()).size();
		Integer houseNumbers = addressRepository.findByHouseNumber(address.getHouseNumber()).size();
		
		CityEntity city = cityServ.getById(address.getCity().getIdCity());
		StreetEntity street = streetServ.getById(address.getStreet().getIdStreet());
		HouseNumberEntity houseNumber = houseNumberServ.getById(address.getHouseNumber().getIdNumber());
		BoroughEntity borough = boroughServ.getById(address.getCity().getBorough().getIdBorough());
		
		if (boroughs > 1) {
			borough = boroughServ.save(new BoroughEntity(ae.getCity().getBorough().getNameBorough(), ae.getCity().getBorough().getNumberBorough()));
		} else {
			borough = boroughServ.update(borough.getIdBorough(), ae.getCity().getBorough());
		}
		if (cities > 1) {
			city = cityServ.save(new CityEntity(ae.getCity().getNameCity(), borough));
		} else {
			city = cityServ.update(city.getIdCity(), ae.getCity());
		}
		if (streets > 1) {
			street = streetServ.save(new StreetEntity(ae.getStreet().getNameStreet()));
		} else {
			street = streetServ.update(street.getIdStreet(), ae.getStreet());
		}
		if (houseNumbers > 1) {
			houseNumber = houseNumberServ.save(new HouseNumberEntity(ae.getHouseNumber().getHouseNumber()));
		} else {
			houseNumber = houseNumberServ.update(houseNumber.getIdNumber(), ae.getHouseNumber());
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

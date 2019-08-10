package com.iktpreobuka.schooldiary.services;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.enums.IRole;
import com.iktpreobuka.schooldiary.repositories.RoleRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String autoDDL;
	
	@EventListener(ApplicationReadyEvent.class)
	public void fillRole() {
		if(autoDDL.equals("create")) {
			for(IRole role : IRole.values()) {
				if(role.equals(IRole.START)) { continue; }
				RoleEntity roleE = new RoleEntity();
				roleE.setRole(role);
				roleRepository.save(roleE);
			}
		}
	}
	
	public RoleEntity getRoleByRole(IRole role) {
		return roleRepository.findByRole(role);
	}
}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.RoleEntity;
import com.iktpreobuka.schooldiary.enums.IRole;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer>{
	RoleEntity findByRole(IRole role);
}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.SuperAdminEntity;
import com.iktpreobuka.schooldiary.securities.Views.SuperAdmin;

@Repository
public interface SuperAdminRepository extends CrudRepository<SuperAdminEntity, Integer> {
	SuperAdminEntity findByEmail(String email);
}

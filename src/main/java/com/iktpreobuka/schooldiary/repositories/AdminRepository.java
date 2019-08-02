package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AdminEntity;

@Repository
public interface AdminRepository extends CrudRepository<AdminEntity, Integer>{

}

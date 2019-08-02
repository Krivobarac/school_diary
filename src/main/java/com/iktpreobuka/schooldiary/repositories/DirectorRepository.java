package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.DirectorEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;

@Repository
public interface DirectorRepository extends CrudRepository<DirectorEntity, Integer> {
	DirectorEntity findByEmail(String email);
	DirectorEntity findBySchool(SchoolEntity school);
}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.DirectorEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;

@Repository
public interface DirectorRepository extends CrudRepository<DirectorEntity, Integer> {
	DirectorEntity findByEmail(String email);
	@Query(value = "select de from DirectorEntity de where de.account != null and de.school = :school")
	DirectorEntity findBySchool(SchoolEntity school);
	DirectorEntity findByAccountUserName(String userName);
}

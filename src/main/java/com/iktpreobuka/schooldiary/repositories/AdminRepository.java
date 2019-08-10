package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AdminEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;

@Repository
public interface AdminRepository extends CrudRepository<AdminEntity, Integer>{
	AdminEntity findByEmail(String email);
	@Query(value = "select ae from AdminEntity ae where ae.account != null and ae.school = :school")
	AdminEntity findBySchool(SchoolEntity school);
	AdminEntity findByAccountUserName(String userName);
}

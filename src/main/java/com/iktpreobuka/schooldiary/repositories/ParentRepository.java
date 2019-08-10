package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;

@Repository
public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {
	ParentEntity findParentEntityByJmbgAndAccountNotNull(String jmbg);
	ParentEntity findByEmailAndStudents(String email, StudentEntity student);
}
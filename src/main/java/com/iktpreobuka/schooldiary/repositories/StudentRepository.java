package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {
	List<StudentEntity> findBySchoolAndAccountNotNull(SchoolEntity school);
	List<StudentEntity> findByParentsAndAccountNotNull(ParentEntity parent);
	List<StudentEntity> findBySchool(SchoolEntity school);
}

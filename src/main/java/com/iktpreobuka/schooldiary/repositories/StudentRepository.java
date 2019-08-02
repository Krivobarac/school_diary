package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.StudentEntity;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

}

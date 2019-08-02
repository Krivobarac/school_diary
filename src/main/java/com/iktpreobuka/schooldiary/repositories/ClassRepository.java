package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ClassEntity;

@Repository
public interface ClassRepository extends CrudRepository<ClassEntity, Integer> {

}

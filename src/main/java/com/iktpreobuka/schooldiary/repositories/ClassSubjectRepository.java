package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ClassSubjectEntity;

@Repository
public interface ClassSubjectRepository extends CrudRepository<ClassSubjectEntity, Integer> {

}

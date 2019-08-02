package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AbsenceEntity;

@Repository
public interface AbsenceRepository extends CrudRepository<AbsenceEntity, Integer> {

}

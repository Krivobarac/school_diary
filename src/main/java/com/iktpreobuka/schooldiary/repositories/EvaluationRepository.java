package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.EvaluationEntity;

@Repository
public interface EvaluationRepository extends CrudRepository<EvaluationEntity, Integer> {

}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ParentEntity;

@Repository
public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {

}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.SchoolYearEntity;

@Repository
public interface SchoolYearRepository extends CrudRepository<SchoolYearEntity, Integer> {

}

package com.iktpreobuka.schooldiary.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.TeacherEntity;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {

}

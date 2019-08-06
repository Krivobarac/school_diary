package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.SubjectEntity;

@Repository
public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {
	List<SubjectEntity> findByIsActive(Boolean activity);
	List<SubjectEntity> findByOptional(Boolean optionality);
	SubjectEntity findBySubjectName(String subjectName);
}

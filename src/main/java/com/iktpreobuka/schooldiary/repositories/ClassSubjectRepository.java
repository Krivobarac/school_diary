package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.iktpreobuka.schooldiary.entities.ClassSubjectEntity;
import com.iktpreobuka.schooldiary.entities.SubjectEntity;

@Repository
public interface ClassSubjectRepository extends CrudRepository<ClassSubjectEntity, Integer> {
	List<ClassSubjectEntity> findBySubject(SubjectEntity subject);
}

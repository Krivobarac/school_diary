package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.SubjectEntity;
import com.iktpreobuka.schooldiary.entities.TeacherEntity;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {
	TeacherEntity findByEmail(String email);
	@Query(value = "select te from TeacherEntity te inner join te.schools s where s = :school")
	List<TeacherEntity> findAllTeachersBySchool(SchoolEntity school);
	List<TeacherEntity> findAllTeachersBySubject(SubjectEntity subject);
	
}

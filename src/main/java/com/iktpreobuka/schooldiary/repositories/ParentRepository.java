package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;

@Repository
public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {
	ParentEntity findParentEntityByJmbgAndAccountNotNull(String jmbg);
	ParentEntity findByEmailAndStudents(String email, StudentEntity student);
	ParentEntity findByEmail(String email);
	List<ParentEntity> findDistinctByStudentsSchoolNumberSchool(Long numberSchool);
	@Query(value = "INSERT INTO student_parent (id_student, id_parent) VALUES (?student, ?parent)", nativeQuery = true)
	ParentEntity setOtherParent(Integer student, Integer parent);
}

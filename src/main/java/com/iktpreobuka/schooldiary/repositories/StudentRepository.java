package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ClassDepartmentEntity;
import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {
	List<StudentEntity> findBySchoolAndAccountNotNull(SchoolEntity school);
	List<StudentEntity> findByParentsAndAccountNotNull(ParentEntity parent);
	List<StudentEntity> findBySchool(SchoolEntity school);
	List<StudentEntity> findByClassDepartments(ClassDepartmentEntity classDepartment);
	List<StudentEntity> findByClassDepartmentsIn(List<ClassDepartmentEntity> classDepartments);
	List<StudentEntity> findByParents(ParentEntity parrent);
	@Query(value = "INSERT INTO student_parent (id_student, id_parent) VALUES (?st, ?pr)", nativeQuery = true)
	Boolean updateStudentParent(Integer st, Integer pr);
}

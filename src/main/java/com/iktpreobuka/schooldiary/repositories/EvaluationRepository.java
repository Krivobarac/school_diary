package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.AdminEntity;
import com.iktpreobuka.schooldiary.entities.EvaluationEntity;
import com.iktpreobuka.schooldiary.entities.ParentEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;
import com.iktpreobuka.schooldiary.entities.TeacherEntity;

@Repository
public interface EvaluationRepository extends CrudRepository<EvaluationEntity, Integer> {
	List<EvaluationEntity> findByTeacher(TeacherEntity teacher);
	List<EvaluationEntity> findByStudentSchoolAdmins(AdminEntity admin);
	List<EvaluationEntity> findByStudentAndStudentSchoolAdmins(StudentEntity student, AdminEntity admin);
	List<EvaluationEntity> findByStudentAndTeacher(StudentEntity student, TeacherEntity teacher);
	List<EvaluationEntity> findByStudent(StudentEntity student);
	EvaluationEntity findByIdEvaluetedAndStudentAndStudentSchoolAdmins(Integer id, StudentEntity student, AdminEntity admin);
	EvaluationEntity findByIdEvaluetedAndStudentAndTeacher(Integer id, StudentEntity student, TeacherEntity teacher);
	List<EvaluationEntity> findByStudentParentsAccountUserName(String username);
	List<EvaluationEntity> findByStudentAccountUserName(String username);
}

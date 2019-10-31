package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.ClassDepartmentEntity;
import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.SchoolYearEntity;
import com.iktpreobuka.schooldiary.enums.IClass;
import com.iktpreobuka.schooldiary.enums.IDepartment;

@Repository
public interface ClassDepartmentRepository extends CrudRepository<ClassDepartmentEntity, Integer> {
	List<ClassDepartmentEntity> findBySchool(SchoolEntity school);
	List<ClassDepartmentEntity> findBySchoolYear(SchoolYearEntity schoolYear);
	List<ClassDepartmentEntity> findBySchoolAndSchoolClassAndSchoolYear(SchoolEntity school, IClass schoolClass, SchoolYearEntity schoolYear);
	ClassDepartmentEntity findBySchoolAndSchoolClassAndSchoolYearAndDepartment(SchoolEntity school, IClass schoolClass, SchoolYearEntity schoolYear, IDepartment department);
	List<ClassDepartmentEntity> findByStudentsAccountNotNull();
	List<ClassDepartmentEntity> findBySchoolInAndSchoolClassIn(List<SchoolEntity> schools, List<IClass> schoolClasses);
}

package com.iktpreobuka.schooldiary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.schooldiary.entities.SchoolEntity;
import com.iktpreobuka.schooldiary.entities.StudentEntity;
import com.iktpreobuka.schooldiary.entities.TeacherEntity;

@Repository
public interface SchoolRepository extends CrudRepository<SchoolEntity, Integer> {
	@Query(value = "select se from SchoolEntity se where se.address = (select ae.idAddress from AddressEntity ae where ae.city = (select ce.idCity from CityEntity ce where ce.nameCity = :nameCity))")
	List<SchoolEntity> findSchoolsByCityName(String nameCity);
	@Query(value = "select se from SchoolEntity se where se.address in (select ae.idAddress from AddressEntity ae where ae.city in (select ce.idCity from CityEntity ce where ce.borough = (select be.idBorough from BoroughEntity be where be.nameBorough = :nameBorough)))")
	List<SchoolEntity> findSchoolsByBorough(String nameBorough);
	@Query(value = "select se from SchoolEntity se inner join se.directors d where d.account = null")
	List<SchoolEntity> findSchoolsNoDirector();
	SchoolEntity findByNumberSchool(Long numberSchool);
	@Query(value = "select se from SchoolEntity se inner join se.admins te inner join te.account ae where ae.userName = :userName")
	SchoolEntity findByAdminsUserName(String userName);
	@Query(value = "select se from SchoolEntity se inner join se.teachers te inner join te.account ae where ae.userName = :userName")
	List<SchoolEntity> findByTeachersUserName(String userName);
	SchoolEntity findByStudents(StudentEntity student);
	List<SchoolEntity> findByTeachers(TeacherEntity teacher);
}

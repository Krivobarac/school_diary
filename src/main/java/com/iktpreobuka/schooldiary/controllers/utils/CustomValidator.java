package com.iktpreobuka.schooldiary.controllers.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.schooldiary.entities.dto.ClassSubjectDTO;
import com.iktpreobuka.schooldiary.entities.dto.SubjectDTO;

//Custom validation class for validating SubjectControler Post and Put methods
@Component
public class CustomValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Object.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Integer field = SubjectDTO.orderNumber;
		Integer subField = ClassSubjectDTO.orderNumber;;
		String text = "Unos redni broj ";
		if (target instanceof SubjectDTO) {
			SubjectDTO subjectDto = (SubjectDTO) target;
			if (subjectDto.getSubjectName().equals(null) || (subjectDto.getSubjectName().trim().equals(""))) {
				errors.reject("400", text + field + ": Predmet je obavezan!");
			}
			if (subjectDto.getClassList().equals(null) || (subjectDto.getClassList().isEmpty())) {
				errors.reject("400", text + field + ": Morate uneti razrede koji sadrze dodeljeni predmet!");
			}
			if (subjectDto.getOptional() == null) {
				errors.reject("400", text + field + ": Morate cekirati opciju!");
			}
		}
		if (target instanceof ClassSubjectDTO) {
			ClassSubjectDTO classSubjectDto = (ClassSubjectDTO) target;
			if (classSubjectDto.getSchoolClass().equals(null) || (classSubjectDto.getSchoolClass().trim().equals(""))) {
				errors.reject("400", text + field + "/" + subField + ": Razred je obavezan!");
			}
			if (classSubjectDto.getFundWeaklyHours() == null) {
				errors.reject("400", text + field + "/" + subField + ": Nedeljni broj casova je obavezan!");
			}
			if (String.valueOf(classSubjectDto.getFundWeaklyHours()).equals(null) || String.valueOf(classSubjectDto.getFundWeaklyHours()).contains("123456789")) {
				errors.reject("400", text + field + "/" + subField + ": Nedeljni broj casova mora biti broj!");
			}
		}
		
	}

}

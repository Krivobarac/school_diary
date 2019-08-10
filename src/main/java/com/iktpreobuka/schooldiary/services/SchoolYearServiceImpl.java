package com.iktpreobuka.schooldiary.services;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.rozidan.springboot.logger.Loggable;
import com.iktpreobuka.schooldiary.entities.SchoolYearEntity;
import com.iktpreobuka.schooldiary.repositories.SchoolYearRepository;

@Loggable(entered = true, warnOver = 2, warnUnit = TimeUnit.SECONDS)
@Service
public class SchoolYearServiceImpl implements SchoolYearService{
	
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String autoDDL;
	
	@Scheduled(cron="0 0 0 30 8 ?")
	@EventListener(ApplicationReadyEvent.class)
	public void fillSchoolYear() {
		if(autoDDL.equals("create")) {
			SchoolYearEntity schoolYear = new SchoolYearEntity();
			Integer year = LocalDateTime.now().getYear();
			schoolYear.setSchoolYear(year + "/" + (year+1));;
			schoolYearRepository.save(schoolYear);
		}
	}
}

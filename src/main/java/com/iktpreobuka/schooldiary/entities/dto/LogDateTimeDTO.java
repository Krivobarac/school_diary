package com.iktpreobuka.schooldiary.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LogDateTimeDTO {
	@NotNull(message = "Pocetni datum pretrage je obavezan!")
	@Pattern(regexp = "^([0-2][0-9]|(3)[0-1])(/)(((0)[0-9])|((1)[0-2]))(/)[0-9]{4}$", message = "Pocetni Datum mora biti ispravan u formatu dd/MM/yyyy")
	private String startDate;
	@NotNull(message = "Pocetno vreme pretrage je obavezno!")
	@Pattern(regexp = "^^(?:[01][0-9]|2[0123]):(?:[012345][0-9]):(?:[012345][0-9])$", message = "Pocetno vreme mora biti ispravno u formatu hh:mm:ss")
	private String startTime;
	@NotNull(message = "Zavrsni datum pretrage je obavezan!")
	@Pattern(regexp = "^([0-2][0-9]|(3)[0-1])(/)(((0)[0-9])|((1)[0-2]))(/)[0-9]{4}$", message = "Zavrsni datum mora biti ispravan u formatu dd/MM/yyyy")
	private String endDate;
	@NotNull(message = "Zavrsno vreme pretrage je obavezno!")
	@Pattern(regexp = "^^(?:[01][0-9]|2[0123]):(?:[012345][0-9]):(?:[012345][0-9])$", message = "Zavrsno vreme mora biti ispravno u formatu hh:mm:ss")
	private String endTime;
	
	public LogDateTimeDTO(String startDate, String startTime, String endDate, String endTime) {
		super();
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.endTime = endTime;
	}

	public LogDateTimeDTO() {
		super();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}

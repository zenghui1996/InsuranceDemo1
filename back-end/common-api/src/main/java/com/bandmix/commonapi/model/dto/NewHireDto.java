package com.bandmix.commonapi.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class NewHireDto {
	
	private List<NewHireMonthDto> listMonth;

	private List<NewHireQuarterDto> ListQuarter;
	
 
}
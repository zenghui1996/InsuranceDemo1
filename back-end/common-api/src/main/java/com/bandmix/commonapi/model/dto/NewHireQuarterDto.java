package com.bandmix.commonapi.model.dto;

import lombok.Data;

@Data
public class NewHireQuarterDto {
	//title
	private String title;

	//1~4季度newHire人数
	private int Quarter1;
	
	private int Quarter2;
	
	private int Quarter3;
	
	private int Quarter4;
	
	

	

}

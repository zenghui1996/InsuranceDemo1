package com.bandmix.commonapi.model.dto;

import lombok.Data;

@Data
public class PromotionMonthListDto {
	

		//1~12月升band人数
		private int Jan;
		
		private int Feb;
		
		private int Mar;
		
		private int Apr;
		
		private int May;
		
		private int Jun;
		 
		private int Jul;
		
		private int Aug;
	 
		private int Sep;
		
		private int Oct;
		
		private int Nov;
		
		private int Dec;	
		
		//每月的总计人数
		private String title;

	
}

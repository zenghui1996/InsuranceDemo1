package com.bandmix.commonapi.model.dto;

import lombok.Data;

@Data
public class PromotionQuarterListDto {
	
	//1~4季度升band人数
	private int Quarter1;
	
	private int Quarter2;
	
	private int Quarter3;
	
	private int Quarter4;
	
	//每季度的总计人数
	private String title;

	
}

package com.bandmix.commonapi.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class PromotionListDto {
	
	//月度list
	private List<PromotionMonthListDto> PromotionDtoMonthList;
	
	//季度list
	private List<PromotionQuarterListDto> PromotionDtoQuarterList;
	
}

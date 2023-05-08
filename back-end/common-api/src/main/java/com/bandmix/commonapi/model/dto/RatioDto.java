package com.bandmix.commonapi.model.dto;

import lombok.Data;

@Data
public class RatioDto {
	
	//计算结果
	private String PromotionDetail = "计算结果";
	
	//比率结果（Promotion (All)）
	private String promotAllRatio;
	
	//比率结果（Promotion (WithoutGH)）
	private String promotWithoutGhRatio;
	
	//比率结果（GH Hire）
	private String ghHireRatio;
	
	//比率结果（GH Hire）
	private String newHireRatio;

	//比率结果（Resign）
	private String resignRatio;

}

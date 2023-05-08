package com.bandmix.commonapi.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ImportCsvDataDto {

	// 组织code
	private String towerCode;
	
	// csv数据结构DTO的List
    private List<CsvDataDto> csvDataDtoList;
	
	// 所在年份
    private String belongingYear;
    
	// 导入方式
    private String importPattern;
}

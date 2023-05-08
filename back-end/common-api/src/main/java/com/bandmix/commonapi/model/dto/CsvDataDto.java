package com.bandmix.commonapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CsvDataDto {

	// 连番
	@JsonProperty("Number")
	private String number;
	
	// 员工号
	@JsonProperty("SN")
    private String employeeSn;
	
	// 员工NotesID
	@JsonProperty("Notes ID")
    private String employeeNotesId;
    
	// 级别
	@JsonProperty("Band")
    private String level;
    
	// 工作地
	@JsonProperty("Location")
    private String location;

	// 性别
	@JsonProperty("M/F")
    private String sex;
    
	// 人事经理Notes
	@JsonProperty("Current PeM")
    private String managerNotesId;
    
    // 当前所在项目
	@JsonProperty("Current Project")
    private String currentProject;
    
    // 当前项目结束日期
	@JsonProperty("End Date")
    private String currentProjectEndTime;
    
    // 下一个项目名
	@JsonProperty("Next Project")
    private String nextProjectName;
    
    // 1月级别
	@JsonProperty("currentYearJan")
    private String januaryLevel;

    // 2月级别
	@JsonProperty("currentYearFeb")
    private String februaryLevel;

    // 3月级别
	@JsonProperty("currentYearMar")
    private String marchLevel;

    // 4月级别
	@JsonProperty("currentYearApr")
    private String aprilLevel;

    // 5月级别
	@JsonProperty("currentYearMay")
    private String mayLevel;

    // 6月级别
	@JsonProperty("currentYearJun")
    private String juneLevel;

    // 7月级别
	@JsonProperty("currentYearJul")
    private String julyLevel;

    // 8月级别
	@JsonProperty("currentYearAug")
    private String augustLevel;

    // 9月级别
	@JsonProperty("currentYearSep")
    private String septemberLevel;

    // 10月级别
	@JsonProperty("currentYearOct")
    private String octoberLevel;

    // 11月级别
	@JsonProperty("currentYearNov")
    private String novemberLevel;

    // 12月级别
	@JsonProperty("currentYearDec")
    private String decemberLevel;
    
    // 明年1月级别
	@JsonProperty("nextYearJan")
    private String nextJanuaryLevel;
    
    // 备注
	@JsonProperty("Comment")
    private String commentDescription;
}

package com.bandmix.commonapi.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BandmixDto {
	
    @JsonProperty("key")
    private String keyNO;
	
    private String searchBelongYear;

    private String searchPemNotesId;

    @JsonProperty("organization")
    private List<String> organizationList;
    
    private String searchBelongTowerCode;

    private String employeeSn;

    private String employeeNotesId;

    private String incrementNumber="123";
    
    private String level;

    private String location;

    private String sex;

    private String peopleManagerNotesId;

    private String currentProject;

    private String currentProjectEnddate;

    private String nextProjectName;

    private String januaryLevel;

    private String februaryLevel;

    private String marchLevel;

    private String aprilLevel;

    private String mayLevel;

    private String juneLevel;

    private String julyLevel;

    private String augustLevel;

    private String septemberLevel;

    private String octoberLevel;

    private String novemberLevel;

    private String decemberLevel;
    
    private String nextJanuaryLevel;

    private String belongingYear;

    private String commnetDescription;

    private String belongingTowerCode;
  	
	private String logicDeleteFlag;

	private String version;
  	
	private String updateUserId;
  	
	private String updateProgramId;
  	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime updateTimestamp;
  	
	private String registerUserId;
  	
	private String registerProgramId;
  	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime registerTimestamp;
}

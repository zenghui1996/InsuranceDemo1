package com.bandmix.commonapi.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserDto {

	private String userId;

	private String userName;

    @JsonProperty("password")
	private String passwordEncrypt;

	private String adminnistratorFlag;

	private String organizationAuthorityCode;

	private String employeeNotesId;

	private String employeeNotesMail;
  	
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
	
	private String oldPassword;
	
	private String newPassword;
	
	private String organizationAuthorityName;
	
	private String adminnistratorFlagStr;
}

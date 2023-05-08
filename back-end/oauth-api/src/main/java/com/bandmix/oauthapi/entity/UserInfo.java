package com.bandmix.oauthapi.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "user_authority_tbl")
public class UserInfo implements Serializable{

	private static final long serialVersionUID =  1L;

	@Id
  	@Column(name="user_id")
	private String userId;

  	@Column(name="user_name")
	private String userName;

  	@Column(name="password_encrypt")
	private String passwordEncrypt;

  	@Column(name="adminnistrator_flag")
	private String adminnistratorFlag;

  	@Column(name="organization_authority_code")
	private String organizationAuthorityCode;

  	@Column(name="employee_notes_id")
	private String employeeNotesId;

  	@Column(name="employee_notes_mail")
	private String employeeNotesMail;
  	
  	@Column(name="logic_delete_flag")
	private String logicDeleteFlag;
  	
  	@Column(name="version")
	private String version;
  	
  	@Column(name="update_user_id")
	private String updateUserId;
  	
  	@Column(name="update_program_id")
	private String updateProgramId;
  	
	@Column(name="update_timestamp")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime updateTimestamp;
  	
  	@Column(name="register_user_id")
	private String registerUserId;
  	
  	@Column(name="register_program_id")
	private String registerProgramId;
  	
  	@Column(name="register_timestamp")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private LocalDateTime registerTimestamp;
}

package com.bandmix.userapi.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_authority_tbl")
@EntityListeners(AuditingEntityListener.class)
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
	@LastModifiedDate
	private LocalDateTime updateTimestamp;
  	
  	@Column(name="register_user_id")
	private String registerUserId;
  	
  	@Column(name="register_program_id")
	private String registerProgramId;
  	
  	@Column(name="register_timestamp")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  	@CreatedDate
	private LocalDateTime registerTimestamp;
}

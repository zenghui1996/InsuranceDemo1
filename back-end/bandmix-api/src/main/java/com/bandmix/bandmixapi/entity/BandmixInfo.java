package com.bandmix.bandmixapi.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "employee_leve_tbl")
@EntityListeners(AuditingEntityListener.class)

@NamedNativeQueries({
		@NamedNativeQuery(name = "BandmixInfo.getBandMixByQuarter", query = "select " + "'9' AS bandLevel,"
				+ " count(case march_level when '9' then 1 end) AS level1Q,"
				+ " count(case june_level when '9' then 1 end) AS level2Q,"
				+ " count(case september_level when '9' then 1 end) AS level3Q,"
				+ " count(case december_level when '9' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'8' AS bandLevel,"
				+ " count(case march_level when '8' then 1 end) AS level1Q,"
				+ " count(case june_level when '8' then 1 end) AS level2Q, "
				+ " count(case september_level when '8' then 1 end) AS level3Q,"
				+ " count(case december_level when '8' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'7B' AS bandLevel,"
				+ " count(case march_level when '7B' then 1 end) AS level1Q,"
				+ " count(case june_level when '7B' then 1 end) AS level2Q,"
				+ " count(case september_level when '7B' then 1 end) AS level3Q,"
				+ " count(case december_level when '7B' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'7A' AS bandLevel,"
				+ " count(case march_level when '7A' then 1 end) AS level1Q,"
				+ " count(case june_level when '7A' then 1 end) AS level2Q,"
				+ " count(case september_level when '7A' then 1 end) AS level3Q,"
				+ " count(case december_level when '7A' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'6B' AS bandLevel,"
				+ " count(case march_level when '6B' then 1 end) AS level1Q,"
				+ " count(case june_level when '6B' then 1 end) AS level2Q,"
				+ " count(case september_level when '6B' then 1 end) AS level3Q,"
				+ " count(case december_level when '6B' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'6A' AS bandLevel,"
				+ " count(case march_level when '6A' then 1 end) AS level1Q,"
				+ " count(case june_level when '6A' then 1 end) AS level2Q,"
				+ " count(case september_level when '6A' then 1 end) AS level3Q,"
				+ " count(case december_level when '6A' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'6G' AS bandLevel,"
				+ " count(case march_level when '6G' then 1 end) AS level1Q,"
				+ " count(case june_level when '6G' then 1 end) AS level2Q,"
				+ " count(case september_level when '6G' then 1 end) AS level3Q,"
				+ " count(case december_level when '6G' then 1  end) AS level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')"
				+ " union all " + "select " + "'Female' AS level,"
				+ "count(case when sex='F' and march_level<>'' then 1 end) level1Q, "
				+ "count(case when sex='F' and june_level<>'' then 1 end) level2Q, "
				+ "count(case when sex='F' and september_level<>'' then 1 end) level3Q, "
				+ "count(case when sex='F' and december_level<>'' then 1 end) level4Q " + " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')", resultSetMapping = "bandMixByQuarter"),

		@NamedNativeQuery(name = "BandmixInfo.getBandMixByMonthly", query = "select" + "'9' AS bandLevel,"
				+ "count(case january_level when '9' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '9' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '9' then 1 end) AS marchLevel,"
				+ "count(case april_level when '9' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '9' then 1 end) AS mayLevel,"
				+ "count(case june_level when '9' then 1 end) AS juneLevel,"
				+ "count(case july_level when '9' then 1 end) AS julyLevel,"
				+ "count(case august_level when '9' then 1 end) AS augustLevel,"
				+ "count(case september_level when '9' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '9' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '9' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '9' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ "union all" +" select " + "'8' AS bandLevel," + "count(case january_level when '8' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '8' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '8' then 1 end) AS marchLevel,"
				+ "count(case april_level when '8' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '8' then 1 end) AS mayLevel,"
				+ "count(case june_level when '8' then 1 end) AS juneLevel,"
				+ "count(case july_level when '8' then 1 end) AS julyLevel,"
				+ "count(case august_level when '8' then 1 end) AS augustLevel,"
				+ "count(case september_level when '8' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '8' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '8' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '8' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ "union all" +" select " + "'7B' AS bandLevel," + "count(case january_level when '7B' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '7B' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '7B' then 1 end) AS marchLevel,"
				+ "count(case april_level when '7B' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '7B' then 1 end) AS mayLevel,"
				+ "count(case june_level when '7B' then 1 end) AS juneLevel,"
				+ "count(case july_level when '7B' then 1 end) AS julyLevel,"
				+ "count(case august_level when '7B' then 1 end) AS augustLevel,"
				+ "count(case september_level when '7B' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '7B' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '7B' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '7B' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ "union all" + " select " +"'7A' AS bandLevel," + "count(case january_level when '7A' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '7A' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '7A' then 1 end) AS marchLevel,"
				+ "count(case april_level when '7A' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '7A' then 1 end) AS mayLevel,"
				+ "count(case june_level when '7A' then 1 end) AS juneLevel,"
				+ "count(case july_level when '7A' then 1 end) AS julyLevel,"
				+ "count(case august_level when '7A' then 1 end) AS augustLevel,"
				+ "count(case september_level when '7A' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '7A' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '7A' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '7A' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ "union all" +" select " + "'6B' AS bandLevel," + "count(case january_level when '6B' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '6B' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '6B' then 1 end) AS marchLevel,"
				+ "count(case april_level when '6B' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '6B' then 1 end) AS mayLevel,"
				+ "count(case june_level when '6B' then 1 end) AS juneLevel,"
				+ "count(case july_level when '6B' then 1 end) AS julyLevel,"
				+ "count(case august_level when '6B' then 1 end) AS augustLevel,"
				+ "count(case september_level when '6B' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '6B' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '6B' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '6B' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ "union all" + " select " +"'6A' AS bandLevel," + "count(case january_level when '6A' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '6A' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '6A' then 1 end) AS marchLevel,"
				+ "count(case april_level when '6A' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '6A' then 1 end) AS mayLevel,"
				+ "count(case june_level when '6A' then 1 end) AS juneLevel,"
				+ "count(case july_level when '6A' then 1 end) AS julyLevel,"
				+ "count(case august_level when '6A' then 1 end) AS augustLevel,"
				+ "count(case september_level when '6A' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '6A' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '6A' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '6A' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ "union all" + " select " +"'6G' AS bandLevel," + "count(case january_level when '6G' then 1 end) AS januaryLevel,"
				+ "count(case february_level when '6G' then 1 end) AS februaryLevel,"
				+ "count(case march_level when '6G' then 1 end) AS marchLevel,"
				+ "count(case april_level when '6G' then 1 end) AS aprilLevel,"
				+ "count(case may_level when '6G' then 1 end) AS mayLevel,"
				+ "count(case june_level when '6G' then 1 end) AS juneLevel,"
				+ "count(case july_level when '6G' then 1 end) AS julyLevel,"
				+ "count(case august_level when '6G' then 1 end) AS augustLevel,"
				+ "count(case september_level when '6G' then 1 end) AS septemberLevel,"
				+ "count(case october_level when '6G' then 1 end) AS octoberLevel,"
				+ "count(case november_level when '6G' then 1 end) AS novemberLevel,"
				+ "count(case december_level when '6G' then 1 end) AS decemberLevel " + "from employee_leve_tbl "
				+ "where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%') "
				+ " union all " + "select " + "'Female' AS level,"
				+ "count(case when sex='F' and january_level<>'' then 1 end) januaryLevel, "
				+ "count(case when sex='F' and february_level<>'' then 1 end) februaryLevel, "
				+ "count(case when sex='F' and march_level<>'' then 1 end) marchLevel, "
				+ "count(case when sex='F' and april_level<>'' then 1 end) aprilLevel,"
				+ "count(case when sex='F' and may_level<>'' then 1 end) mayLevel,"
				+ "count(case when sex='F' and june_level<>'' then 1 end) juneLevel,"
				+ "count(case when sex='F' and july_level<>'' then 1 end) julyLevel,"
				+ "count(case when sex='F' and august_level<>'' then 1 end) augustLevel,"
				+ "count(case when sex='F' and september_level<>'' then 1 end) septemberLevel,"
				+ "count(case when sex='F' and october_level<>'' then 1 end) octoberLevel,"
				+ "count(case when sex='F' and november_level<>'' then 1 end) novemberLevel,"
				+ "count(case when sex='F' and december_level<>'' then 1 end) decemberLevel "
				+ " from employee_leve_tbl"
				+ " where belonging_year=:belongingYear  and belonging_tower_code like CONCAT(:towerCode,'%')", resultSetMapping = "bandmixMonthly") })

@SqlResultSetMappings({

		@SqlResultSetMapping(name = "bandmixMonthly", classes = {
				@ConstructorResult(targetClass = com.bandmix.commonapi.model.dto.BandmixMonthlyDto.class, columns = {
						@ColumnResult(name = "bandLevel"), @ColumnResult(name = "januaryLevel"),
						@ColumnResult(name = "februaryLevel"), @ColumnResult(name = "marchLevel"),
						@ColumnResult(name = "aprilLevel"), @ColumnResult(name = "mayLevel"),
						@ColumnResult(name = "juneLevel"), @ColumnResult(name = "julyLevel"),
						@ColumnResult(name = "augustLevel"), @ColumnResult(name = "septemberLevel"),
						@ColumnResult(name = "octoberLevel"), @ColumnResult(name = "novemberLevel"),
						@ColumnResult(name = "decemberLevel") }) }),
		@SqlResultSetMapping(name = "bandMixByQuarter", classes = {
				@ConstructorResult(targetClass = com.bandmix.commonapi.model.dto.BandMixQuarterDto.class, columns = {
						@ColumnResult(name = "bandLevel"), @ColumnResult(name = "level1Q"),
						@ColumnResult(name = "level2Q"), @ColumnResult(name = "level3Q"),
						@ColumnResult(name = "level4Q") }) }) }

)

public class BandmixInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BandmixInfoKey id;

	@Column(name = "employee_notes_id")
	private String employeeNotesId;

	@Column(name = "increment_number")
	private String incrementNumber;

	@Column(name = "level")
	private String level;

	@Column(name = "location")
	private String location;

	@Column(name = "sex")
	private String sex;

	@Column(name = "current_project")
	private String currentProject;

	@Column(name = "current_project_enddate")
	private String currentProjectEnddate;

	@Column(name = "next_project_name")
	private String nextProjectName;

	@Column(name = "january_level")
	private String januaryLevel;

	@Column(name = "february_level")
	private String februaryLevel;

	@Column(name = "march_level")
	private String marchLevel;

	@Column(name = "april_level")
	private String aprilLevel;

	@Column(name = "may_level")
	private String mayLevel;

	@Column(name = "june_level")
	private String juneLevel;

	@Column(name = "july_level")
	private String julyLevel;

	@Column(name = "august_level")
	private String augustLevel;

	@Column(name = "september_level")
	private String septemberLevel;

	@Column(name = "october_level")
	private String octoberLevel;

	@Column(name = "november_level")
	private String novemberLevel;

	@Column(name = "december_level")
	private String decemberLevel;

	@Column(name = "commnet_description")
	private String commnetDescription;

	@Column(name = "next_january_level")
	private String nextJanuaryLevel;

	@Column(name = "logic_delete_flag")
	private String logicDeleteFlag;

	@Column(name = "version")
	private String version;

	@Column(name = "update_user_id")
	private String updateUserId;

	@Column(name = "update_program_id")
	private String updateProgramId;

	@Column(name = "update_timestamp")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	@LastModifiedDate
	private LocalDateTime updateTimestamp;

	@Column(name = "register_user_id")
	private String registerUserId;

	@Column(name = "register_program_id")
	private String registerProgramId;

	@Column(name = "register_timestamp")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	@CreatedDate
	private LocalDateTime registerTimestamp;
}

package com.bandmix.userapi.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "organization_code_tbl")
@EntityListeners(AuditingEntityListener.class)
public class OrganizationInfo implements Serializable{

	private static final long serialVersionUID =  1L;

    @EmbeddedId
    private OrganizationfoKey id;

    @Column(name = "organization_code_description")
    private String organizationCodeDescription;

    @Column(name = "sector_code_description")
    private String sectorCodeDescription;

    @Column(name = "tower_code_description")
    private String towerCodeDescription;

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

package com.bandmix.bandmixapi.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class OrganizationfoKey implements Serializable{

	private static final long serialVersionUID =  1L;

    @Column(name = "organization_code", columnDefinition = "char(3)")
    private String organizationCode;

    @Column(name = "sector_code", columnDefinition = "char(3)")
    private String sectorCode;

    @Column(name = "tower_code", columnDefinition = "char(3)")
    private String towerCode;
}

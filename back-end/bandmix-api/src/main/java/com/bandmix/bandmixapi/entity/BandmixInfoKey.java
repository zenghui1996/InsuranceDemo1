package com.bandmix.bandmixapi.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class BandmixInfoKey implements Serializable{

	private static final long serialVersionUID =  1L;

    @Column(name = "employee_sn", columnDefinition = "varchar(9)")
    private String employeeSn;

    @Column(name = "people_manager_notes_id", columnDefinition = "varchar(128)")
    private String peopleManagerNotesId;

    @Column(name = "belonging_year", columnDefinition = "varchar(4)")
    private String belongingYear;

    @Column(name = "belonging_tower_code", columnDefinition = "varchar(9)")
    private String belongingTowerCode;

}

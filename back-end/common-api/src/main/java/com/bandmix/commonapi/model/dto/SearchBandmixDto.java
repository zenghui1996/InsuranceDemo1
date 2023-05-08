package com.bandmix.commonapi.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SearchBandmixDto {
    @JsonProperty("organization")
//    @NotNull(message="组织code不能为空！")
    private List<String> organizationList;
//    @NotNull(message="所在年不能为空！")
	private LocalDate yearMouth;

}

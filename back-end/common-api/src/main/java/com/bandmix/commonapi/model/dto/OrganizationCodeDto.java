package com.bandmix.commonapi.model.dto;

import lombok.Data;

@Data
public class OrganizationCodeDto {
	// 编号
	private String value;

	// 表述
	private String label;

	// 父节点编号
	private String pid;
}

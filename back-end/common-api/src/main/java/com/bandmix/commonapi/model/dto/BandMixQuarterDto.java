package com.bandmix.commonapi.model.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BandMixQuarterDto {
	private String bandLevel;	
	
	private String level1Q;	
	
	private String level2Q;		
	
	private String level3Q;		
	
	private String level4Q;	
	
	public BandMixQuarterDto() {
	}

	public BandMixQuarterDto( Object bandLevel, BigInteger level1Q, BigInteger level2Q, BigInteger level3Q, BigInteger level4Q) {
		this.bandLevel = String.valueOf(bandLevel);
		this.level1Q = String.valueOf(level1Q);
		this.level2Q = String.valueOf(level2Q);
		this.level3Q = String.valueOf(level3Q);
		this.level4Q = String.valueOf(level4Q);
	}

}

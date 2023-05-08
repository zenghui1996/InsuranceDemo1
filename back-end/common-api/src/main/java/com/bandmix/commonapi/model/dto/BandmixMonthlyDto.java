package com.bandmix.commonapi.model.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class BandmixMonthlyDto {

	private String bandLevel;

	private String dataJan;

	private String dataFeb;

	private String dataMar;

	private String dataApr;

	private String dataMay;

	private String dataJun;

	private String dataJul;

	private String dataAug;

	private String dataSep;

	private String dataOct;

	private String dataNov;

	private String dataDec;

	public BandmixMonthlyDto() {

	}

	public BandmixMonthlyDto(String bandLevel, BigInteger dataJan, BigInteger dataFeb, BigInteger dataMar,
			BigInteger dataApr, BigInteger dataMay, BigInteger dataJun, BigInteger dataJul, BigInteger dataAug,
			BigInteger dataSep, BigInteger dataOct, BigInteger dataNov, BigInteger dataDec) {
		this.bandLevel = bandLevel;
		this.dataJan = String.valueOf(dataJan);
		this.dataFeb = String.valueOf(dataFeb);
		this.dataMar = String.valueOf(dataMar);
		this.dataApr = String.valueOf(dataApr);
		this.dataMay = String.valueOf(dataMay);
		this.dataJun = String.valueOf(dataJun);
		this.dataJul = String.valueOf(dataJul);
		this.dataAug = String.valueOf(dataAug);
		this.dataSep = String.valueOf(dataSep);
		this.dataOct = String.valueOf(dataOct);
		this.dataNov = String.valueOf(dataNov);
		this.dataDec = String.valueOf(dataDec);

	}
}

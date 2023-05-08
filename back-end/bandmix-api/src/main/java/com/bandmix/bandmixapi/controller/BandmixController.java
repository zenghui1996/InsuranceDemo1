package com.bandmix.bandmixapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bandmix.bandmixapi.service.BandmixInfoService;
import com.bandmix.commonapi.model.dto.BandmixDto;
import com.bandmix.commonapi.model.dto.ImportCsvDataDto;
import com.bandmix.commonapi.model.dto.SearchBandmixDto;
import com.bandmix.commonapi.model.reponse.Result;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BandmixController {

	@Autowired
	private BandmixInfoService bandmixInfoService;

	@GetMapping("/test")
	public String registry() {
int a =9;
a=10;
		return "very well!";
	}

	@GetMapping("/organizationCode")
	public ResponseEntity<Result> organizationCode(@RequestHeader("Authorization") String authorization) {

		// 处理概要：
		// 调用Bandmix服务类的组织Code取得方法（BandmixInfoService.organizationCode）, 返回结果
		Result result = bandmixInfoService.organizationCode(authorization);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// csv导入
	@PostMapping("/dataManage")
	public ResponseEntity<Result> importCsvData(@RequestBody ImportCsvDataDto dto) {
		Result result = bandmixInfoService.importCsvData(dto);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 员工查询
	@PostMapping("/dataList")
	public ResponseEntity<Result> getEmployeeSearch(@RequestBody BandmixDto bandmixDto) {

		List<String> organizationList = new ArrayList<>();
		organizationList = bandmixDto.getOrganizationList();

		if (organizationList != null && organizationList.size() > 0) {
			String searchBelongTowerCode = String.join("", bandmixDto.getOrganizationList()).replace("all", "");
			bandmixDto.setSearchBelongTowerCode(searchBelongTowerCode);
			Result result = getDataList(bandmixDto);

			return new ResponseEntity<Result>(result, HttpStatus.OK);
		} else {

			return new ResponseEntity<Result>(Result.error("请选择部门！"), HttpStatus.OK);
		}

	}

	// 员工登录
	@PutMapping("/employeesave")
	public ResponseEntity<Result> persistEmployeeData(@RequestHeader("Authorization") String authorization,
			@RequestBody BandmixDto bandmixDto) {
		bandmixInfoService.persistEmployeeData(authorization, bandmixDto);

		Result result = getDataList(bandmixDto);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 员工删除
	@DeleteMapping("/employeedelete")
	public ResponseEntity<Result> deleteEmployeeData(@RequestBody BandmixDto bandmixDto) {
		Result result = bandmixInfoService.deleteEmployeeData(bandmixDto);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 月别
	@PostMapping("/getBandmixMonthly")
	public Object getBandmixMonthly(@RequestBody SearchBandmixDto searchBandmixDto) {
		String searchBelongTowerCode = String.join("", searchBandmixDto.getOrganizationList()).replace("all", "");
		Result result = bandmixInfoService.getBandmixSearch(searchBelongTowerCode,
				String.valueOf(searchBandmixDto.getYearMouth().getYear()));
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 季度别
	@PostMapping("/getBandMixByQuarter")
	public ResponseEntity<Result> getBandMixByQuarter(@RequestBody SearchBandmixDto searchBandmixDto) {
		String searchBelongTowerCode = String.join("", searchBandmixDto.getOrganizationList()).replace("all", "");
		Result result = bandmixInfoService
				.getBandMixByQuarter(String.valueOf(searchBandmixDto.getYearMouth().getYear()), searchBelongTowerCode);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	// 数据一览画面检索用
	private Result getDataList(BandmixDto bandmixDto) {
		Result result = null;
		if (!StringUtils.isEmpty(bandmixDto.getSearchPemNotesId())) {
			result = bandmixInfoService.getEmployeeSearch(bandmixDto.getSearchBelongTowerCode(),
					bandmixDto.getSearchPemNotesId(), bandmixDto.getSearchBelongYear());
		} else {
			result = bandmixInfoService.getEmployeeSearch1(bandmixDto.getSearchBelongTowerCode(),
					bandmixDto.getSearchBelongYear());
		}
		return result;
	}

	/**
	 * 
	 * 新入职人员的信息查询
	 * 
	 * @param belongingYear 检索条件年，towerCode 检索条件所在组织
	 * @return 新入职人员的月/季的统计信息
	 * 
	 */
	@PostMapping("/newHire")
	public ResponseEntity<Result> getNewHire(@RequestBody Map<String, Object> map) {
//		log.info("getNewHire Start "+ "belongingYear: "+ map.get("belongingYear")+ "organization : "+
//				map.get("organization"));
		String searchBelongTowerCode = String.join("", (List) map.get("organization")).replace("all", "");
		Result result = bandmixInfoService.getNewHireSearch(map.get("belongingYear").toString(), searchBelongTowerCode);
//		log.info("getNewHire end", result);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	/**
	 * 
	 * 离职人员的信息查询
	 * 
	 * @param belongingYear 检索条件年，towerCode 检索条件所在组织
	 * @return 离职人员的月/季的统计信息
	 * 
	 */
	@PostMapping("/resign")
	public ResponseEntity<Result> getResign(@RequestBody Map<String, Object> map) {
		log.info("getResign start");
		String towerCode = String.join("", (List) map.get("organization")).replace("all", "");
		String belongingYear = (String) map.get("belongingYear");

		log.info("belongingYear: ", belongingYear, "towerCode : ", towerCode);

		// 调用检索
		Result result = bandmixInfoService.getResignSearch(belongingYear, towerCode);

		log.info("getResign end", result);

		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}
	
	/**
	 * 
	 * 晋升人员的统计信息
	 * 
	 * @param authorization Token, map.belongingYear 检索条件年
	 * @return 月/季的晋升人员的统计信息
	 * 
	 */
    @PostMapping("/promotion")
    public ResponseEntity<Result> getPromotionSearch(@RequestHeader("Authorization") String authorization, @RequestBody Map<String,String> map) {
    	
//    	log.info("getPomotion Start ", "belongingYear: ", map.get("belongingYear"), "towerCode : ", map.get("towerCode"));
		
		Result result = bandmixInfoService.getPromotionSearch(authorization, map.get("belongingYear"));
    	
//    	log.info("getPomotion end", result);
    	
    	return new ResponseEntity<Result>(result, HttpStatus.OK);
    }
    
    /**
     * 
     * 比率计算
     * 
     * @param map belongingYear 检索条件年，towerCode 检索条件所在组织
     * @return 对应查询比率类型的计算结果
     * 
     */
    @PostMapping("/ratioStatics")
    public Object getRatioStatics(@RequestHeader("Authorization") String authorization, @RequestBody Map<String,String> map) {
    	
//    	log.info("getRatioStatics Start ", "belongingYear: ", map.get("belongingYear"), "towerCode : ", map.get("towerCode"));
    	
		Result result = bandmixInfoService.getRatioStatics(authorization, map.get("belongingYear"));
    	
		
//    	log.info("getRatioStatics end", result);
    	
    	return new ResponseEntity<Result>(result, HttpStatus.OK);
    }

}

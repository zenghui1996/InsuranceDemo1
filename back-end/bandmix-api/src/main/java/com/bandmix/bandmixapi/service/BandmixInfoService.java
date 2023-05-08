package com.bandmix.bandmixapi.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bandmix.bandmixapi.entity.BandmixInfo;
import com.bandmix.bandmixapi.entity.BandmixInfoKey;
import com.bandmix.bandmixapi.entity.OrganizationInfo;
import com.bandmix.bandmixapi.entity.UserToken;
import com.bandmix.bandmixapi.repository.BandmixInfoRepository;
import com.bandmix.bandmixapi.repository.OrganizationInfoRepository;
import com.bandmix.bandmixapi.repository.UserTokenRepository;
import com.bandmix.commonapi.model.dto.BandMixQuarterDto;
import com.bandmix.commonapi.model.dto.BandmixDto;
import com.bandmix.commonapi.model.dto.BandmixMonthlyDto;
import com.bandmix.commonapi.model.dto.CsvDataDto;
import com.bandmix.commonapi.model.dto.ImportCsvDataDto;
import com.bandmix.commonapi.model.dto.NewHireDto;
import com.bandmix.commonapi.model.dto.NewHireMonthDto;
import com.bandmix.commonapi.model.dto.NewHireQuarterDto;
import com.bandmix.commonapi.model.dto.OrganizationCodeDto;
import com.bandmix.commonapi.model.dto.SearchBandmixDto;
import com.bandmix.commonapi.model.reponse.Result;
import com.bandmix.commonapi.model.dto.PromotionDto;
import com.bandmix.commonapi.model.dto.PromotionListDto;
import com.bandmix.commonapi.model.dto.PromotionMonthListDto;
import com.bandmix.commonapi.model.dto.PromotionQuarterListDto;
import com.bandmix.commonapi.model.dto.RatioDto;
import com.bandmix.commonapi.model.dto.RatioListDto;
import com.bandmix.commonapi.model.reponse.Result;

@Service
public class BandmixInfoService {

	@Autowired
	private BandmixInfoRepository bandmixInfoRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private UserTokenRepository userTokenRepository;

	public Result organizationCode(String authorization) {
		// token取得
		UserToken userToken = userTokenRepository.findByTokenId(authorization);

		// 管理者Flag取得
		String userAdminnistratorFlag = userToken.getAdminnistratorFlag();
		// 组织权限code
		String organizationAuthorityCode = userToken.getOrganizationAuthorityCode();

		// 组织Code
		List<OrganizationInfo> organizationCodeList = new ArrayList<>();
		// 返回数据
		List<OrganizationCodeDto> OrganizationCodeDtoListResult = new ArrayList<>();
		// 编号Map
		Map<String, String> organizationCodeMap = new HashMap<>();

		// 处理概要：
		// 1.1 取得组织Code
		// 1.1.1 参数.管理者Flag = [2：Acount管理者]或[3：超级管理者]的场合
		if ("2".equals(userAdminnistratorFlag) || "3".equals(userAdminnistratorFlag)) {
			// 调用BandMix存储接口的组织Code取得（全部）（BandmixInfoRepository.findAll）
			// 调用参数：无
			// 返回结果：List<com.bandmix.commonapi.entity.OrganizationCode>
			organizationCodeList = organizationInfoRepository.findAll();
		}

		// 1.1.2 参数.管理者Flag = [1：普通用户]
		if ("1".equals(userAdminnistratorFlag)) {
			// （1）参数.组织权限code的长度是3的场合
			if (organizationAuthorityCode.length() == 3) {
				// 调用参数：参数.组织权限code的前三位
				String userAdminnistratorFlagTopThree = organizationAuthorityCode.substring(0);

				// 调用BandMix存储接口的组织Code取得（组织别）（BandmixInfoRepository.findByOrganizationCode）
				// 返回结果：List<com.bandmix.commonapi.entity.OrganizationCode>
				organizationCodeList = organizationInfoRepository
						.findByIdOrganizationCode(userAdminnistratorFlagTopThree);
			}

			// （2）参数.组织权限code的长度是6的场合
			if (organizationAuthorityCode.length() == 6) {
				// 调用参数：参数.组织权限code的前三位、参数.组织权限code的后三位
				String userAdminnistratorFlagTopThree = organizationAuthorityCode.substring(0, 3);
				String userAdminnistratorFlagEndThree = organizationAuthorityCode.substring(3, 6);

				// 调用BandMix存储接口的组织Code取得（部门别）（BandmixInfoRepository.findByOrganizationCodeAndSectorCode）
				// 返回结果：List<com.bandmix.commonapi.entity.OrganizationCode>
				organizationCodeList = organizationInfoRepository.findByIdOrganizationCodeAndIdSectorCode(
						userAdminnistratorFlagTopThree, userAdminnistratorFlagEndThree);
			}

			// （3）参数.组织权限code的长度是9的场合
			if (organizationAuthorityCode.length() == 9) {
				// 调用参数：参数.组织权限code的前三位、参数.组织权限code的中间三位、参数.组织权限code的后三位
				String userAdminnistratorFlagTopThree = organizationAuthorityCode.substring(0, 3);
				String userAdminnistratorFlagMiddleThree = organizationAuthorityCode.substring(3, 6);
				String userAdminnistratorFlagEndThree = organizationAuthorityCode.substring(6, 9);

				// 调用BandMix存储接口的组织Code取得（Tower别）（BandmixInfoRepository.findByOrganizationCodeAndSectorCodeAndTowerCode）
				// 返回结果：List<com.bandmix.commonapi.entity.OrganizationCode>
				organizationCodeList = organizationInfoRepository.findByIdOrganizationCodeAndIdSectorCodeAndIdTowerCode(
						userAdminnistratorFlagTopThree, userAdminnistratorFlagMiddleThree,
						userAdminnistratorFlagEndThree);
			}
		}

		// 1.1.3 List<OrganizationCode>的件数为0的场合
		if (organizationCodeList.size() == 0) {
			// 返回值.处理结果 = false
			// 返回值.异常信息 = "Message0009"（组织信息不存在!）
			// 处理终了
			return Result.error("Message0009");
		}

		// 返回数据
		OrganizationCodeDto organizationCodeDto = new OrganizationCodeDto();
		// 1.2
		// 循环List<OrganizationCode>，返回数据设定com.bandmix.commonapi.model.dto.OrganizationCodeDto
		for (OrganizationInfo organizationCode : organizationCodeList) {
			// 1.2.1 编号Map里OrganizationCode.组织编号不存在的场合
			if (!organizationCodeMap.containsKey(organizationCode.getId().getOrganizationCode())) {
				// 返回数据
				organizationCodeDto = new OrganizationCodeDto();
				// 编号 = OrganizationCode.组织编号
				organizationCodeDto.setValue(organizationCode.getId().getOrganizationCode());
				// 编号表述 = OrganizationCode.组织编号表述
				organizationCodeDto.setLabel(organizationCode.getOrganizationCodeDescription());
				// 父节点编号 = “”
				organizationCodeDto.setPid("");

				OrganizationCodeDtoListResult.add(organizationCodeDto);

				// 编号Map
				// OrganizationCode.组织编号
				organizationCodeMap.put(organizationCode.getId().getOrganizationCode(), "");

				// 参数.管理者Flag = [2：Acount管理者]或[3：超级管理者]
				// 或，（参数.管理者Flag = [1：普通用户]，且，参数.组织权限code的长度是3）
				if (("2".equals(userAdminnistratorFlag) || "3".equals(userAdminnistratorFlag))
						|| ("1".equals(userAdminnistratorFlag) && organizationAuthorityCode.length() == 3)) {
					// 返回数据
					organizationCodeDto = new OrganizationCodeDto();
					// 编号 = “all”
					organizationCodeDto.setValue("all");
					// 编号表述 = “所有部门”
					organizationCodeDto.setLabel("所有部门");
					// 父节点编号 = OrganizationCode.组织编号
					organizationCodeDto.setPid(organizationCode.getId().getOrganizationCode());

					OrganizationCodeDtoListResult.add(organizationCodeDto);
				}
			}

			// 1.2.2 编号Map里OrganizationCode.部门编号不存在的场合
			if (!organizationCodeMap.containsKey(organizationCode.getId().getSectorCode())) {
				// 返回数据
				organizationCodeDto = new OrganizationCodeDto();
				// 编号 = OrganizationCode.部门编号
				organizationCodeDto.setValue(organizationCode.getId().getSectorCode());
				// 编号表述 = OrganizationCode.部门编号表述
				organizationCodeDto.setLabel(organizationCode.getSectorCodeDescription());
				// 父节点编号 = OrganizationCode.组织编号
				organizationCodeDto.setPid(organizationCode.getId().getOrganizationCode());

				OrganizationCodeDtoListResult.add(organizationCodeDto);

				// 编号Map
				// OrganizationCode.部门编号
				organizationCodeMap.put(organizationCode.getId().getSectorCode(), "");

				// 参数.管理者Flag = [2：Acount管理者]或[3：超级管理者]
				// 或，（参数.管理者Flag = [1：普通用户]，且，参数.组织权限code的长度是3或6）
				if (("2".equals(userAdminnistratorFlag) || "3".equals(userAdminnistratorFlag)) || ("1"
						.equals(userAdminnistratorFlag)
						&& (organizationAuthorityCode.length() == 3 || organizationAuthorityCode.length() == 6))) {
					// 返回数据
					organizationCodeDto = new OrganizationCodeDto();
					// 编号 = “all”
					organizationCodeDto.setValue("all");
					// 编号表述 = “所有Tower”
					organizationCodeDto.setLabel("所有Tower");
					// 父节点编号 = OrganizationCode.部门编号
					organizationCodeDto.setPid(organizationCode.getId().getSectorCode());

					OrganizationCodeDtoListResult.add(organizationCodeDto);
				}
			}

			// 1.2.3 编号Map里OrganizationCode.Tower编号不存在的场合
			if (!organizationCodeMap.containsKey(organizationCode.getId().getTowerCode())) {
				// 返回数据
				organizationCodeDto = new OrganizationCodeDto();
				// 编号 = OrganizationCode.Tower编号
				organizationCodeDto.setValue(organizationCode.getId().getTowerCode());
				// 编号表述 = OrganizationCode.Tower编号表述
				organizationCodeDto.setLabel(organizationCode.getTowerCodeDescription());
				// 父节点编号 = OrganizationCode.部门编号
				organizationCodeDto.setPid(organizationCode.getId().getSectorCode());

				OrganizationCodeDtoListResult.add(organizationCodeDto);

				// 编号Map
				// OrganizationCode.Tower编号
				organizationCodeMap.put(organizationCode.getId().getTowerCode(), "");
			}
		}

		// 1.3 返回结果设定
		// 返回值.处理结果 = true
		// 返回值.返回数据 =
		// 1.2中设定后的返回数据List<com.bandmix.commonapi.model.dto.OrganizationCodeDto>
		// 处理终了
		return Result.success(OrganizationCodeDtoListResult);
	}

	/**
	 * csv导入
	 * 
	 * @return result [O] 返回结果
	 */
	@Transactional
	public Result importCsvData(ImportCsvDataDto dto) {
		// 数据导入件数
		int inputNum = 0;
		// Skip件数
		int skipNum = 0;

		// 【导入方式】是【全量导入：2】的时候
		if ("2".equals(dto.getImportPattern())) {
			// 删除符合以下条件的数据
			// 组织code
			// 所在年份
			bandmixInfoRepository.deleteByIdBelongingTowerCodeAndIdBelongingYear(dto.getTowerCode(),
					dto.getBelongingYear());

			// 数据导入
			for (CsvDataDto csvDataDto : dto.getCsvDataDtoList()) {
				BandmixInfo bandmixInfo = new BandmixInfo();
				// 连番
				bandmixInfo.setIncrementNumber(csvDataDto.getNumber());
				// 员工NotesID
				bandmixInfo.setEmployeeNotesId(csvDataDto.getEmployeeNotesId());
				// 级别
				bandmixInfo.setLevel(csvDataDto.getLevel());
				// 工作地
				bandmixInfo.setLocation(csvDataDto.getLocation());
				// 性别
				bandmixInfo.setSex(csvDataDto.getSex());
				// 当前所在项目
				bandmixInfo.setCurrentProject(csvDataDto.getCurrentProject());
				// 当前项目结束日期
				bandmixInfo.setCurrentProjectEnddate(csvDataDto.getCurrentProjectEndTime());
				// 下一个项目名
				if (csvDataDto.getNextProjectName() == null) {
					bandmixInfo.setNextProjectName("");
				} else {
					bandmixInfo.setNextProjectName(csvDataDto.getNextProjectName());
				}
				// 1月级别
				bandmixInfo.setJanuaryLevel(csvDataDto.getJanuaryLevel());
				// 2月级别
				bandmixInfo.setFebruaryLevel(csvDataDto.getFebruaryLevel());
				// 3月级别.
				bandmixInfo.setMarchLevel(csvDataDto.getMarchLevel());
				// 4月级别
				bandmixInfo.setAprilLevel(csvDataDto.getAprilLevel());
				// 5月级别
				bandmixInfo.setMayLevel(csvDataDto.getMayLevel());
				// 6月级别
				bandmixInfo.setJuneLevel(csvDataDto.getJuneLevel());
				// 7月级别
				bandmixInfo.setJulyLevel(csvDataDto.getJulyLevel());
				// 8月级别
				bandmixInfo.setAugustLevel(csvDataDto.getAugustLevel());
				// 9月级别
				bandmixInfo.setSeptemberLevel(csvDataDto.getSeptemberLevel());
				// 10月级别
				bandmixInfo.setOctoberLevel(csvDataDto.getOctoberLevel());
				// 11月级别
				bandmixInfo.setNovemberLevel(csvDataDto.getNovemberLevel());
				// 12月级别
				bandmixInfo.setDecemberLevel(csvDataDto.getDecemberLevel());
				// 来年1月级别
				bandmixInfo.setNextJanuaryLevel(csvDataDto.getNextJanuaryLevel());
				// 备注
				bandmixInfo.setCommnetDescription(csvDataDto.getCommentDescription());
				// Key
				BandmixInfoKey bandInfoId = new BandmixInfoKey();
				// 组织code
				bandInfoId.setBelongingTowerCode(dto.getTowerCode());
				// 所在年份
				bandInfoId.setBelongingYear(dto.getBelongingYear());
				// 员工号
				bandInfoId.setEmployeeSn(csvDataDto.getEmployeeSn());
				// 人事经理Notes
				bandInfoId.setPeopleManagerNotesId(csvDataDto.getManagerNotesId());

				bandmixInfo.setId(bandInfoId);

				bandmixInfo.setLogicDeleteFlag("0");
				bandmixInfo.setVersion("1");
				bandmixInfo.setUpdateUserId("1");
				bandmixInfo.setUpdateProgramId("1");
				bandmixInfo.setRegisterUserId("1");
				bandmixInfo.setRegisterProgramId("1");

				// 数据导入
				bandmixInfoRepository.save(bandmixInfo);
				inputNum++;
			}
			// 【导入方式】是【差分导入：1】的时候
		} else if ("1".equals(dto.getImportPattern())) {
			// 数据导入
			for (CsvDataDto csvDataDto : dto.getCsvDataDtoList()) {
				// Key
				BandmixInfoKey bandMixInfoId = new BandmixInfoKey();
				// 组织code
				bandMixInfoId.setBelongingTowerCode(dto.getTowerCode());
				// 所在年份
				bandMixInfoId.setBelongingYear(dto.getBelongingYear());
				// 员工号
				bandMixInfoId.setEmployeeSn(csvDataDto.getEmployeeSn());
				// 人事经理Notes
				bandMixInfoId.setPeopleManagerNotesId(csvDataDto.getManagerNotesId());

				Optional<BandmixInfo> bandmixInfo = bandmixInfoRepository.findById(bandMixInfoId);

				// 数据不存在
				if (bandmixInfo.isEmpty()) {
					BandmixInfo outBandmixInfo = new BandmixInfo();
					// 连番
					outBandmixInfo.setIncrementNumber(csvDataDto.getNumber());
					// 员工NotesID
					outBandmixInfo.setEmployeeNotesId(csvDataDto.getEmployeeNotesId());
					// 级别
					outBandmixInfo.setLevel(csvDataDto.getLevel());
					// 工作地
					outBandmixInfo.setLocation(csvDataDto.getLocation());
					// 性别
					outBandmixInfo.setSex(csvDataDto.getSex());
					// 当前所在项目
					outBandmixInfo.setCurrentProject(csvDataDto.getCurrentProject());
					// 当前项目结束日期
					outBandmixInfo.setCurrentProjectEnddate(csvDataDto.getCurrentProjectEndTime());
					// 下一个项目名
					if (csvDataDto.getNextProjectName() == null) {
						outBandmixInfo.setNextProjectName("");
					} else {
						outBandmixInfo.setNextProjectName(csvDataDto.getNextProjectName());
					}
					// 1月级别
					outBandmixInfo.setJanuaryLevel(csvDataDto.getJanuaryLevel());
					// 2月级别
					outBandmixInfo.setFebruaryLevel(csvDataDto.getFebruaryLevel());
					// 3月级别.
					outBandmixInfo.setMarchLevel(csvDataDto.getMarchLevel());
					// 4月级别
					outBandmixInfo.setAprilLevel(csvDataDto.getAprilLevel());
					// 5月级别
					outBandmixInfo.setMayLevel(csvDataDto.getMayLevel());
					// 6月级别
					outBandmixInfo.setJuneLevel(csvDataDto.getJuneLevel());
					// 7月级别
					outBandmixInfo.setJulyLevel(csvDataDto.getJulyLevel());
					// 8月级别
					outBandmixInfo.setAugustLevel(csvDataDto.getAugustLevel());
					// 9月级别
					outBandmixInfo.setSeptemberLevel(csvDataDto.getSeptemberLevel());
					// 10月级别
					outBandmixInfo.setOctoberLevel(csvDataDto.getOctoberLevel());
					// 11月级别
					outBandmixInfo.setNovemberLevel(csvDataDto.getNovemberLevel());
					// 12月级别
					outBandmixInfo.setDecemberLevel(csvDataDto.getDecemberLevel());
					// 来年1月级别
					outBandmixInfo.setNextJanuaryLevel(csvDataDto.getNextJanuaryLevel());
					// 备注
					outBandmixInfo.setCommnetDescription(csvDataDto.getCommentDescription());

					outBandmixInfo.setId(bandMixInfoId);

					outBandmixInfo.setLogicDeleteFlag("0");
					outBandmixInfo.setVersion("1");
					outBandmixInfo.setUpdateUserId("1");
					outBandmixInfo.setUpdateProgramId("1");
					outBandmixInfo.setRegisterUserId("1");
					outBandmixInfo.setRegisterProgramId("1");

					// 数据导入
					bandmixInfoRepository.save(outBandmixInfo);
					inputNum++;
				} else {
					skipNum++;
				}
			}
		}

		// 返回结果处理
		String resultStr = "csv总件数=" + dto.getCsvDataDtoList().size() + "件\n";
		resultStr += "导入成功件数=" + inputNum + "件\n";
		resultStr += "Skip件数=" + skipNum + "件";

		return Result.success(resultStr);
	}

	// 员工查询
	public Result getEmployeeSearch(String organizationCode, String currentPeM, String currentyear) {

		List<String> checkList = new ArrayList<>();

		if (StringUtils.isEmpty(currentyear)) {
			checkList.add("请输入所在年份！");
		}

		if (checkList != null && checkList.size() > 0) {
			return Result.checkError(checkList);
		}

		String searchCurrentPeM = currentPeM.replace("*", "/");
		List<BandmixInfo> employeeList = bandmixInfoRepository
				.findByBelongingTowerCodeAndPeopleManagerNotesidAndBelongingYear(organizationCode, searchCurrentPeM,
						currentyear);

		List<BandmixDto> bandmixDtoList = setBandmixDtoList(organizationCode, currentPeM, currentyear, employeeList);

		if (!bandmixDtoList.isEmpty()) {
			return Result.success(bandmixDtoList);
		} else {
			return Result.error("员工信息不存在");
		}
	}

	// 员工查询(Pem条件除外)
	public Result getEmployeeSearch1(String organizationCode, String currentyear) {

		List<String> checkList = new ArrayList<>();

		if (StringUtils.isEmpty(currentyear)) {
			checkList.add("请输入所在年份！");
		}

		if (checkList != null && checkList.size() > 0) {
			return Result.checkError(checkList);
		}

		List<BandmixInfo> employeeList = bandmixInfoRepository
				.findByBelongingTowerCodeAndPemNullAndBelongingYear(organizationCode, currentyear);

		List<BandmixDto> bandmixDtoList = setBandmixDtoList(organizationCode, null, currentyear, employeeList);

		if (!bandmixDtoList.isEmpty()) {
			return Result.success(bandmixDtoList);
		} else {
			return Result.error("员工信息不存在");
		}
	}

	// 员工登录
	public Result persistEmployeeData(String authorization, BandmixDto employee) {

		BandmixInfo bandmixInfoEntity = new BandmixInfo();

		BeanUtils.copyProperties(employee, bandmixInfoEntity);

		BandmixInfoKey key = new BandmixInfoKey();
		key.setEmployeeSn(employee.getEmployeeSn());
		key.setPeopleManagerNotesId(employee.getPeopleManagerNotesId());
		key.setBelongingYear(employee.getSearchBelongYear());
		key.setBelongingTowerCode(employee.getSearchBelongTowerCode());
		bandmixInfoEntity.setId(key);

//        TODO
		// token取得
		UserToken userToken = userTokenRepository.findByTokenId(authorization);

		bandmixInfoEntity.setCommnetDescription("CommnetDescription");
		bandmixInfoEntity.setNextProjectName(employee.getCurrentProject());
		bandmixInfoEntity.setUpdateUserId(userToken.getUserId());
		bandmixInfoEntity.setUpdateProgramId("SJ");
		bandmixInfoEntity.setRegisterUserId(userToken.getUserId());
		bandmixInfoEntity.setRegisterProgramId("SJ");

		bandmixInfoEntity.setLogicDeleteFlag("1");
		bandmixInfoEntity.setVersion("1");
		bandmixInfoEntity.setUpdateTimestamp(LocalDateTime.now());
		bandmixInfoEntity.setRegisterTimestamp(LocalDateTime.now());

		bandmixInfoRepository.save(bandmixInfoEntity);
		return Result.success("新员工登陆成功！");

	}

	// 员工删除
	public Result deleteEmployeeData(BandmixDto bandmixDto) {

		BandmixInfoKey key = new BandmixInfoKey();
		key.setEmployeeSn(bandmixDto.getEmployeeSn());
		key.setPeopleManagerNotesId(bandmixDto.getPeopleManagerNotesId());
		key.setBelongingYear(bandmixDto.getSearchBelongYear());
		key.setBelongingTowerCode(bandmixDto.getSearchBelongTowerCode());

		bandmixInfoRepository.deleteById(key);

		return Result.success("员工数据删除成功！");

	}

	public Result getBandMixByQuarter(String belongingYear, String towerCode) {

		List<BandMixQuarterDto> bandMixQuarterDtoList = bandmixInfoRepository.getBandMixByQuarter(belongingYear,
				towerCode);

		if (bandMixQuarterDtoList.isEmpty()) {
			return Result.success("");
		}

		Map<String, Long> countMap = new HashMap();
		countMap.put("1Q", 0L);
		countMap.put("2Q", 0L);
		countMap.put("3Q", 0L);
		countMap.put("4Q", 0L);

		Map<String, Double> sumBandmixMap = new HashMap();
		sumBandmixMap.put("1Q", 0.0);
		sumBandmixMap.put("2Q", 0.0);
		sumBandmixMap.put("3Q", 0.0);
		sumBandmixMap.put("4Q", 0.0);

		BandMixQuarterDto fRatioDto = new BandMixQuarterDto();

		for (BandMixQuarterDto bandMixQuarterDto : bandMixQuarterDtoList) {

			switch (bandMixQuarterDto.getBandLevel()) {
			case "9":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 9.0);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 9.0);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 9.0);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 9.0);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));

				break;
			case "8":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 8.0);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 8.0);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 8.0);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 8.0);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));
				break;
			case "7B":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 7.5);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 7.5);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 7.5);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 7.5);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));
				break;
			case "7A":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 7.0);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 7.0);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 7.0);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 7.0);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));
				break;
			case "6B":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 6.5);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 6.5);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 6.5);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 6.5);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));
				break;
			case "6A":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 6.0);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 6.0);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 6.0);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 6.0);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));
				break;
			case "6G":
				sumBandmixMap.put("1Q", sumBandmixMap.get("1Q") + Double.valueOf(bandMixQuarterDto.getLevel1Q()) * 5.5);
				sumBandmixMap.put("2Q", sumBandmixMap.get("2Q") + Double.valueOf(bandMixQuarterDto.getLevel2Q()) * 5.5);
				sumBandmixMap.put("3Q", sumBandmixMap.get("3Q") + Double.valueOf(bandMixQuarterDto.getLevel3Q()) * 5.5);
				sumBandmixMap.put("4Q", sumBandmixMap.get("4Q") + Double.valueOf(bandMixQuarterDto.getLevel4Q()) * 5.5);

				countMap.put("1Q", countMap.get("1Q") + Integer.valueOf(bandMixQuarterDto.getLevel1Q()));
				countMap.put("2Q", countMap.get("2Q") + Integer.valueOf(bandMixQuarterDto.getLevel2Q()));
				countMap.put("3Q", countMap.get("3Q") + Integer.valueOf(bandMixQuarterDto.getLevel3Q()));
				countMap.put("4Q", countMap.get("4Q") + Integer.valueOf(bandMixQuarterDto.getLevel4Q()));
				break;
			case "Female":
				fRatioDto = bandMixQuarterDto;
				break;
			default:
				break;
			}
		}

		fRatioDto.setBandLevel("Female ratio");
    	fRatioDto.setLevel1Q(scaleData(fRatioDto.getLevel1Q(),countMap.get("1Q")));
    	fRatioDto.setLevel2Q(scaleData(fRatioDto.getLevel2Q(),countMap.get("2Q")));
    	fRatioDto.setLevel3Q(scaleData(fRatioDto.getLevel3Q(),countMap.get("3Q")));
    	fRatioDto.setLevel4Q(scaleData(fRatioDto.getLevel4Q(),countMap.get("4Q")));
    	
		BandMixQuarterDto bandMixDto  = new BandMixQuarterDto();
		bandMixDto.setBandLevel("BandMix");
		bandMixDto.setLevel1Q(scaleData(sumBandmixMap.get("1Q"),countMap.get("1Q")));
		bandMixDto.setLevel2Q(scaleData(sumBandmixMap.get("2Q"),countMap.get("2Q")));
		bandMixDto.setLevel3Q(scaleData(sumBandmixMap.get("3Q"),countMap.get("3Q")));
		bandMixDto.setLevel4Q(scaleData(sumBandmixMap.get("4Q"),countMap.get("4Q")));
		bandMixQuarterDtoList.add(7,bandMixDto);

		BandMixQuarterDto sumFteDto = new BandMixQuarterDto();
		sumFteDto.setBandLevel("Sum FTE");
		sumFteDto.setLevel1Q(String.valueOf(countMap.get("1Q")));
		sumFteDto.setLevel2Q(String.valueOf(countMap.get("2Q")));
		sumFteDto.setLevel3Q(String.valueOf(countMap.get("3Q")));
		sumFteDto.setLevel4Q(String.valueOf(countMap.get("4Q")));
		bandMixQuarterDtoList.add(8, sumFteDto);

		return Result.success(bandMixQuarterDtoList);
	}

	public Result getBandmixSearch(String towerCode, String belongingYear) {

		List<BandmixMonthlyDto> bandmixMonthlyDtoList = bandmixInfoRepository
				.findByBelongingYearAndBelongingTowerCodeLike(towerCode, belongingYear);

		if (bandmixMonthlyDtoList.size() == 0) {
			return Result.error("band不存在!");
		}

		Map<String, Long> countMap = new HashMap();
		for (int i = 1; i < 13; i++) {
			countMap.put(i + "M", 0L);
		}

		Map<String, Double> sumBandmixMap = new HashMap();
		for (int i = 1; i < 13; i++) {
			sumBandmixMap.put(i + "M", 0.0);
		}

		BandmixMonthlyDto fRatioDto = new BandmixMonthlyDto();

		for (BandmixMonthlyDto bandmixMonthlyDto : bandmixMonthlyDtoList) {

			switch (bandmixMonthlyDto.getBandLevel()) {
			case "9":
				setMap(sumBandmixMap, countMap, 9.0, bandmixMonthlyDto);
				break;
			case "8":
				setMap(sumBandmixMap, countMap, 8.0, bandmixMonthlyDto);
				break;
			case "7B":
				setMap(sumBandmixMap, countMap, 7.5, bandmixMonthlyDto);
				break;
			case "7A":
				setMap(sumBandmixMap, countMap, 7.0, bandmixMonthlyDto);
				break;
			case "6B":
				setMap(sumBandmixMap, countMap, 6.5, bandmixMonthlyDto);
				break;
			case "6A":
				setMap(sumBandmixMap, countMap, 6.0, bandmixMonthlyDto);
				break;
			case "6G":
				setMap(sumBandmixMap, countMap, 5.5, bandmixMonthlyDto);
				break;
			case "Female":
				fRatioDto = bandmixMonthlyDto;
				break;
			default:
				break;
			}
		}

		fRatioDto.setBandLevel("Female ratio");
		fRatioDto.setDataJan(scaleData(fRatioDto.getDataJan(), countMap.get("1M")));
		fRatioDto.setDataFeb(scaleData(fRatioDto.getDataFeb(), countMap.get("2M")));
		fRatioDto.setDataMar(scaleData(fRatioDto.getDataMar(), countMap.get("3M")));
		fRatioDto.setDataApr(scaleData(fRatioDto.getDataApr(), countMap.get("4M")));
		fRatioDto.setDataMay(scaleData(fRatioDto.getDataMay(), countMap.get("5M")));
		fRatioDto.setDataJun(scaleData(fRatioDto.getDataJun(), countMap.get("6M")));
		fRatioDto.setDataJul(scaleData(fRatioDto.getDataJul(), countMap.get("7M")));
		fRatioDto.setDataAug(scaleData(fRatioDto.getDataAug(), countMap.get("8M")));
		fRatioDto.setDataSep(scaleData(fRatioDto.getDataSep(), countMap.get("9M")));
		fRatioDto.setDataOct(scaleData(fRatioDto.getDataOct(), countMap.get("10M")));
		fRatioDto.setDataNov(scaleData(fRatioDto.getDataNov(), countMap.get("11M")));
		fRatioDto.setDataDec(scaleData(fRatioDto.getDataDec(), countMap.get("12M")));

		BandmixMonthlyDto bandMixDto = new BandmixMonthlyDto();
		bandMixDto.setBandLevel("BandMix");
		bandMixDto.setDataJan(scaleData(sumBandmixMap.get("1M"), countMap.get("1M")));
		bandMixDto.setDataFeb(scaleData(sumBandmixMap.get("2M"), countMap.get("2M")));
		bandMixDto.setDataMar(scaleData(sumBandmixMap.get("3M"), countMap.get("3M")));
		bandMixDto.setDataApr(scaleData(sumBandmixMap.get("4M"), countMap.get("4M")));
		bandMixDto.setDataMay(scaleData(sumBandmixMap.get("5M"), countMap.get("5M")));
		bandMixDto.setDataJun(scaleData(sumBandmixMap.get("6M"), countMap.get("6M")));
		bandMixDto.setDataJul(scaleData(sumBandmixMap.get("7M"), countMap.get("7M")));
		bandMixDto.setDataAug(scaleData(sumBandmixMap.get("8M"), countMap.get("8M")));
		bandMixDto.setDataSep(scaleData(sumBandmixMap.get("9M"), countMap.get("9M")));
		bandMixDto.setDataOct(scaleData(sumBandmixMap.get("10M"), countMap.get("10M")));
		bandMixDto.setDataNov(scaleData(sumBandmixMap.get("11M"), countMap.get("11M")));
		bandMixDto.setDataDec(scaleData(sumBandmixMap.get("12M"), countMap.get("12M")));
		bandmixMonthlyDtoList.add(7, bandMixDto);

		BandmixMonthlyDto sumFteDto = new BandmixMonthlyDto();
		sumFteDto.setBandLevel("Sum FTE");
		sumFteDto.setDataJan(String.valueOf(countMap.get("1M")));
		sumFteDto.setDataFeb(String.valueOf(countMap.get("2M")));
		sumFteDto.setDataMar(String.valueOf(countMap.get("3M")));
		sumFteDto.setDataApr(String.valueOf(countMap.get("4M")));
		sumFteDto.setDataMay(String.valueOf(countMap.get("5M")));
		sumFteDto.setDataJun(String.valueOf(countMap.get("6M")));
		sumFteDto.setDataJul(String.valueOf(countMap.get("7M")));
		sumFteDto.setDataAug(String.valueOf(countMap.get("8M")));
		sumFteDto.setDataSep(String.valueOf(countMap.get("9M")));
		sumFteDto.setDataOct(String.valueOf(countMap.get("10M")));
		sumFteDto.setDataNov(String.valueOf(countMap.get("11M")));
		sumFteDto.setDataDec(String.valueOf(countMap.get("12M")));
		bandmixMonthlyDtoList.add(8, sumFteDto);

		return Result.success(bandmixMonthlyDtoList);
	}

	/**
	 * 
	 * 新入职人员的信息查询
	 * 
	 * @param belongingYear 检索条件年，towerCode 检索条件所在组织
	 * @return 新入职人员的月/季的统计信息
	 * 
	 */
	public Result getNewHireSearch(String belongingYear, String towerCode) {

		NewHireDto newHireDto = new NewHireDto();

		// 月度Associate对象
		NewHireMonthDto associateMonthDto = new NewHireMonthDto();

		// 月度Associate以外对象
		NewHireMonthDto associateExceptMonthDto = new NewHireMonthDto();

		// 月度总计的对象
		NewHireMonthDto totalMonthDto = new NewHireMonthDto();

		// 季度Associate对象
		NewHireQuarterDto associateQuarterDto = new NewHireQuarterDto();

		// 季度Associate以外对象
		NewHireQuarterDto associateExceptQuarterDto = new NewHireQuarterDto();

		// 季度总计的对象
		NewHireQuarterDto totalQuarterDto = new NewHireQuarterDto();

		// 从DB中检索
		List<Object[]> newHireList = bandmixInfoRepository
				.findNewHireByBelongingYearAndBelongingTowerCodeLike(belongingYear, towerCode);
		// 检索条件当年的结果（去年12月份，今年1月，2月，~12月 from employee_leve_tbl）
		if (newHireList != null && newHireList.size() > 0) {

			// 遍历list
			for (Object[] bandmixNewHireInfo : newHireList) {

				// 去年12月级别
				String beforyersdecember = (String) bandmixNewHireInfo[0];
				if ((beforyersdecember == null) || ("-".equals(beforyersdecember)) || (" ".equals(beforyersdecember))) {
					beforyersdecember = "";
					}

				// 1月级别
				String januaryLevel = (String) bandmixNewHireInfo[1];
				if ((januaryLevel == null) || ("-".equals(januaryLevel)) || (" ".equals(januaryLevel))) {
					januaryLevel = "";
				}

				// 2月级别
				String februaryLevel = (String) bandmixNewHireInfo[2];
				if ((februaryLevel == null) || ("-".equals(februaryLevel)) || (" ".equals(februaryLevel))) {
					februaryLevel = "";
				}

				// 3月级别
				String marchLevel = (String) bandmixNewHireInfo[3];
				if ((marchLevel == null) || ("-".equals(marchLevel)) || (" ".equals(marchLevel))) {
					marchLevel = "";
				}

				// 4月级别
				String aprilLevel = (String) bandmixNewHireInfo[4];
				if ((aprilLevel == null) || ("-".equals(aprilLevel)) || (" ".equals(aprilLevel))) {
					aprilLevel = "";
				}

				// 5月级别
				String mayLevel = (String) bandmixNewHireInfo[5];
				if ((mayLevel == null) || ("-".equals(mayLevel)) || (" ".equals(mayLevel))) {
					mayLevel = "";
				}

				// 6月级别
				String juneLevel = (String) bandmixNewHireInfo[6];
				if ((juneLevel == null) || ("-".equals(juneLevel)) || (" ".equals(juneLevel))) {
					juneLevel = "";
				}

				// 7月级别
				String julyLevel = (String) bandmixNewHireInfo[7];
				if ((julyLevel == null) || ("-".equals(julyLevel)) || (" ".equals(julyLevel))) {
					julyLevel = "";
				}

				// 8月级别
				String augustLevel = (String) bandmixNewHireInfo[8];
				if ((augustLevel == null) || ("-".equals(augustLevel)) || (" ".equals(augustLevel))) {
					augustLevel = "";
				}

				// 9月级别
				String septemberLevel = (String) bandmixNewHireInfo[9];
				if ((septemberLevel == null) || ("-".equals(septemberLevel)) || (" ".equals(septemberLevel))) {
					septemberLevel = "";
				}

				// 10月级别
				String octoberLevel = (String) bandmixNewHireInfo[10];
				if ((octoberLevel == null) || ("-".equals(octoberLevel)) || (" ".equals(octoberLevel))) {
					octoberLevel = "";
				}

				// 11月级别
				String novemberLevel = (String) bandmixNewHireInfo[11];
				if ((novemberLevel == null) || ("-".equals(novemberLevel)) || (" ".equals(novemberLevel))) {
					novemberLevel = "";
				}

				// 12月级别
				String decemberLevel = (String) bandmixNewHireInfo[12];
				if ((decemberLevel == null) || ("-".equals(decemberLevel)) || (" ".equals(decemberLevel))) {
					decemberLevel = "";
				}

				// 判断level是否为6G
				if (StringUtils.isEmpty(beforyersdecember) && !StringUtils.isEmpty(januaryLevel)){
					if (("6G").equals(januaryLevel)) {
						associateMonthDto.setJan(associateMonthDto.getJan() + 1);
					} else {
						associateExceptMonthDto.setJan(associateExceptMonthDto.getJan() + 1);
					}
				}

				if (StringUtils.isEmpty(januaryLevel) && !StringUtils.isEmpty(februaryLevel)){
					if (("6G").equals(februaryLevel)) {
						associateMonthDto.setFeb(associateMonthDto.getFeb() + 1);
					} else {
						associateExceptMonthDto.setFeb(associateExceptMonthDto.getFeb() + 1);
					}
				}
				
				if(StringUtils.isEmpty(februaryLevel) && !StringUtils.isEmpty(marchLevel)) {
					if (("6G").equals(marchLevel)) {
						associateMonthDto.setMar(associateMonthDto.getMar() + 1);
					} else {
						associateExceptMonthDto.setMar(associateExceptMonthDto.getMar() + 1);
					}
				}

				if (StringUtils.isEmpty(marchLevel) && !StringUtils.isEmpty(aprilLevel)){
					if (("6G").equals(aprilLevel)) {
						associateMonthDto.setApr(associateMonthDto.getApr() + 1);
					} else {
						associateExceptMonthDto.setApr(associateExceptMonthDto.getApr() + 1);
					}
				}

				if (StringUtils.isEmpty(aprilLevel) && !StringUtils.isEmpty(mayLevel)) {
					if (("6G").equals(mayLevel)) {
						associateMonthDto.setMay(associateMonthDto.getMay() + 1);
					} else {
						associateExceptMonthDto.setMay(associateExceptMonthDto.getMay() + 1);
					}
				}

				if (StringUtils.isEmpty(mayLevel) && !StringUtils.isEmpty(juneLevel)){
					if (("6G").equals(juneLevel)) {
						associateMonthDto.setJun(associateMonthDto.getJun() + 1);
					} else {
						associateExceptMonthDto.setJun(associateExceptMonthDto.getJun() + 1);
					}
				}

				if (StringUtils.isEmpty(juneLevel) && !StringUtils.isEmpty(julyLevel)){
					if (("6G").equals(julyLevel)) {
						associateMonthDto.setJul(associateMonthDto.getJul() + 1);
					} else {
						associateExceptMonthDto.setJul(associateExceptMonthDto.getJul() + 1);
					}
				}

				if (StringUtils.isEmpty(julyLevel) && !StringUtils.isEmpty(augustLevel)) {
					if (("6G").equals(augustLevel)) {
						associateMonthDto.setAug(associateMonthDto.getAug() + 1);
					} else {
						associateExceptMonthDto.setAug(associateExceptMonthDto.getAug() + 1);
					}
				}

				if (StringUtils.isEmpty(augustLevel) && !StringUtils.isEmpty(septemberLevel)) {
					if (("6G").equals(septemberLevel)) {
						associateMonthDto.setSep(associateMonthDto.getSep() + 1);
					} else {
						associateExceptMonthDto.setSep(associateExceptMonthDto.getSep() + 1);
					}
				}

				if(StringUtils.isEmpty(septemberLevel) && !StringUtils.isEmpty(octoberLevel)) {
					if (("6G").equals(octoberLevel)) {
						associateMonthDto.setOct(associateMonthDto.getOct() + 1);
					} else {
						associateExceptMonthDto.setOct(associateExceptMonthDto.getOct() + 1);
					}
				}

				if (StringUtils.isEmpty(octoberLevel) && !StringUtils.isEmpty(novemberLevel)) {
					if (("6G").equals(novemberLevel)) {
						associateMonthDto.setNov(associateMonthDto.getNov() + 1);
					} else {
						associateExceptMonthDto.setNov(associateExceptMonthDto.getNov() + 1);
					}
				}

				if (StringUtils.isEmpty(novemberLevel) && !StringUtils.isEmpty(decemberLevel)) {
					if (("6G").equals(decemberLevel)) {
						associateMonthDto.setDec(associateMonthDto.getDec() + 1);
					} else {
						associateExceptMonthDto.setDec(associateExceptMonthDto.getDec() + 1);
					}
				}
			}

			// 1月合计数
			totalMonthDto.setJan(associateMonthDto.getJan() + associateExceptMonthDto.getJan());

			// 2月合计数
			totalMonthDto.setFeb(associateMonthDto.getFeb() + associateExceptMonthDto.getFeb());

			// 3月合计数
			totalMonthDto.setMar(associateMonthDto.getMar() + associateExceptMonthDto.getMar());

			// 4月合计数
			totalMonthDto.setApr(associateMonthDto.getApr() + associateExceptMonthDto.getApr());

			// 5月合计数
			totalMonthDto.setMay(associateMonthDto.getMay() + associateExceptMonthDto.getMay());

			// 6月合计数
			totalMonthDto.setJun(associateMonthDto.getJun() + associateExceptMonthDto.getJun());

			// 7月合计数
			totalMonthDto.setJul(associateMonthDto.getJul() + associateExceptMonthDto.getJul());

			// 8月合计数
			totalMonthDto.setAug(associateMonthDto.getAug() + associateExceptMonthDto.getAug());

			// 9月合计数
			totalMonthDto.setSep(associateMonthDto.getSep() + associateExceptMonthDto.getSep());

			// 10月合计数
			totalMonthDto.setOct(associateMonthDto.getOct() + associateExceptMonthDto.getOct());

			// 11月合计数
			totalMonthDto.setNov(associateMonthDto.getNov() + associateExceptMonthDto.getNov());

			// 12月合计数
			totalMonthDto.setDec(associateMonthDto.getDec() + associateExceptMonthDto.getDec());

			// 月度Associate标题
			associateMonthDto.setTitle("Associate");

			// 月度Associate以外标题
			associateExceptMonthDto.setTitle("Associate以外");

			int TotalNum1 = totalMonthDto.getJan() + totalMonthDto.getFeb() + totalMonthDto.getMar()
					+ totalMonthDto.getApr() + totalMonthDto.getMay() + totalMonthDto.getJun() + totalMonthDto.getJul()
					+ totalMonthDto.getAug() + totalMonthDto.getSep() + totalMonthDto.getOct() + totalMonthDto.getNov()
					+ totalMonthDto.getDec();

			// 月度总计标题
			totalMonthDto.setTitle(belongingYear + "Total:" + TotalNum1);

			// 1季度associate合计数
			associateQuarterDto
					.setQuarter1(associateMonthDto.getJan() + associateMonthDto.getFeb() + associateMonthDto.getMar());

			// 2季度associate合计数
			associateQuarterDto
					.setQuarter2(associateMonthDto.getApr() + associateMonthDto.getMay() + associateMonthDto.getJun());

			// 3季度associate合计数
			associateQuarterDto
					.setQuarter3(associateMonthDto.getJul() + associateMonthDto.getAug() + associateMonthDto.getSep());

			// 4季度associate合计数
			associateQuarterDto
					.setQuarter4(associateMonthDto.getOct() + associateMonthDto.getNov() + associateMonthDto.getDec());

			// 1季度associate以外合计数
			associateExceptQuarterDto.setQuarter1(associateExceptMonthDto.getJan() + associateExceptMonthDto.getFeb()
					+ associateExceptMonthDto.getMar());

			// 2季度associate以外合计数
			associateExceptQuarterDto.setQuarter2(associateExceptMonthDto.getApr() + associateExceptMonthDto.getMay()
					+ associateExceptMonthDto.getJun());

			// 3季度associate以外合计数
			associateExceptQuarterDto.setQuarter3(associateExceptMonthDto.getJul() + associateExceptMonthDto.getAug()
					+ associateExceptMonthDto.getSep());

			// 4季度associate以外合计数
			associateExceptQuarterDto.setQuarter4(associateExceptMonthDto.getOct() + associateExceptMonthDto.getNov()
					+ associateExceptMonthDto.getDec());

			// 1季度合计数
			totalQuarterDto.setQuarter1(associateQuarterDto.getQuarter1() + associateExceptQuarterDto.getQuarter1());

			// 2季度合计数
			totalQuarterDto.setQuarter2(associateQuarterDto.getQuarter2() + associateExceptQuarterDto.getQuarter2());

			// 3季度合计数
			totalQuarterDto.setQuarter3(associateQuarterDto.getQuarter3() + associateExceptQuarterDto.getQuarter3());

			// 4季度合计数
			totalQuarterDto.setQuarter4(associateQuarterDto.getQuarter4() + associateExceptQuarterDto.getQuarter4());

			// 季度Associate标题
			associateQuarterDto.setTitle("Associate");

			// 季度Associate以外标题
			associateExceptQuarterDto.setTitle("Associate以外");

			int TotalNum2 = totalQuarterDto.getQuarter1() + totalQuarterDto.getQuarter2()
					+ totalQuarterDto.getQuarter3() + totalQuarterDto.getQuarter4();

			// 季度总计标题
			totalQuarterDto.setTitle(belongingYear + "Total:" + TotalNum2);

			// 月度的list集合
			List<NewHireMonthDto> listMonthTmp = new ArrayList<NewHireMonthDto>();

			listMonthTmp.add(associateMonthDto);

			listMonthTmp.add(associateExceptMonthDto);

			listMonthTmp.add(totalMonthDto);

			// 季度的list集合
			List<NewHireQuarterDto> listQuarterTmp = new ArrayList<NewHireQuarterDto>();

			listQuarterTmp.add(associateQuarterDto);

			listQuarterTmp.add(associateExceptQuarterDto);

			listQuarterTmp.add(totalQuarterDto);

			// 将月度的list集合放入newHireDto中
			newHireDto.setListMonth(listMonthTmp);

			// 将季度的list集合放入newHireDto中
			newHireDto.setListQuarter(listQuarterTmp);

			return Result.success(newHireDto);
		}

		return Result.error("检索结果零件，请重新设定检索条件。");
		
	}

	/**
	 * 
	 * 离职人员的信息查询
	 * 
	 * @param belongingYear 检索条件年，towerCode 检索条件所在组织
	 * @return 离职人员的月/季的统计信息
	 * 
	 */
	public Result getResignSearch(String belongingYear, String towerCode) {

		// 返回结果定义
		NewHireDto newHireDto = new NewHireDto();

		NewHireMonthDto associateMonthDto = new NewHireMonthDto();
		NewHireMonthDto associateExceptMonthDto = new NewHireMonthDto();
		NewHireMonthDto totalMonthDto = new NewHireMonthDto();

		NewHireQuarterDto associateQuarterDto = new NewHireQuarterDto();
		NewHireQuarterDto associateExceptQuarterDto = new NewHireQuarterDto();
		NewHireQuarterDto totalQuarterDto = new NewHireQuarterDto();

		// 调用检索的SQL
		List<Object[]> promotionList = bandmixInfoRepository
				.findResignByBelongingYearAndBelongingTowerCodeLike(belongingYear, towerCode);

		// 检索结果存在的情况（去年12月份，今年1月，2月，~12月 from employee_leve_tbl）
		if (promotionList != null && promotionList.size() > 0) {

			// 遍历检索结果的每一条数据，进行累计统计
			for (Object[] bandmixPromotioInfo : promotionList) {

				// 去年12月级别
				String beforyersdecember = (String) bandmixPromotioInfo[1];
				if ((beforyersdecember == null) || ("-".equals(beforyersdecember)) || (" ".equals(beforyersdecember))) {
					beforyersdecember = "";
				}
				// 1月级别
				String januaryLevel = (String) bandmixPromotioInfo[2];
				if ((januaryLevel == null) || ("-".equals(januaryLevel)) || (" ".equals(januaryLevel))) {
					januaryLevel = "";
				}

				// 2月级别
				String februaryLevel = (String) bandmixPromotioInfo[3];
				if ((februaryLevel == null) || ("-".equals(februaryLevel)) || (" ".equals(februaryLevel))) {
					februaryLevel = "";
				}

				// 3月级别
				String marchLevel = (String) bandmixPromotioInfo[4];
				if ((marchLevel == null) || ("-".equals(marchLevel)) || (" ".equals(marchLevel))) {
					marchLevel = "";
				}

				// 4月级别
				String aprilLevel = (String) bandmixPromotioInfo[5];
				if ((aprilLevel == null) || ("-".equals(aprilLevel)) || (" ".equals(aprilLevel))) {
					aprilLevel = "";
				}

				// 5月级别
				String mayLevel = (String) bandmixPromotioInfo[6];
				if ((mayLevel == null) || ("-".equals(mayLevel)) || (" ".equals(mayLevel))) {
					mayLevel = "";
				}

				// 6月级别
				String juneLevel = (String) bandmixPromotioInfo[7];
				if ((juneLevel == null) || ("-".equals(juneLevel)) || (" ".equals(juneLevel))) {
					juneLevel = "";
				}

				// 7月级别
				String julyLevel = (String) bandmixPromotioInfo[8];
				if ((julyLevel == null) || ("-".equals(julyLevel)) || (" ".equals(julyLevel))) {
					julyLevel = "";
				}

				// 8月级别
				String augustLevel = (String) bandmixPromotioInfo[9];
				if ((augustLevel == null) || ("-".equals(augustLevel)) || (" ".equals(augustLevel))) {
					augustLevel = "";
				}

				// 9月级别
				String septemberLevel = (String) bandmixPromotioInfo[10];
				if ((septemberLevel == null) || ("-".equals(septemberLevel)) || (" ".equals(septemberLevel))) {
					septemberLevel = "";
				}

				// 10月级别
				String octoberLevel = (String) bandmixPromotioInfo[11];
				if ((octoberLevel == null) || ("-".equals(octoberLevel)) || (" ".equals(octoberLevel))) {
					octoberLevel = "";
				}

				// 11月级别
				String novemberLevel = (String) bandmixPromotioInfo[12];
				if ((novemberLevel == null) || ("-".equals(novemberLevel)) || (" ".equals(novemberLevel))) {
					novemberLevel = "";
				}

				// 12月级别
				String decemberLevel = (String) bandmixPromotioInfo[13];
				if ((decemberLevel == null) || ("-".equals(decemberLevel)) || (" ".equals(decemberLevel))) {
					decemberLevel = "";
				}

				// 前一個月不爲空，後一個月爲空
				if (!StringUtils.isEmpty(beforyersdecember.trim()) && StringUtils.isEmpty(januaryLevel.trim())) {
					// 判斷是否是6G員工
					if (("6G").equals(beforyersdecember)) {
						associateMonthDto.setJan(associateMonthDto.getJan() + 1);
					} else {
						associateExceptMonthDto.setJan(associateExceptMonthDto.getJan() + 1);
					}
				}

				if (!StringUtils.isEmpty(januaryLevel.trim()) && StringUtils.isEmpty(februaryLevel.trim())) {
					if (("6G").equals(januaryLevel)) {
						associateMonthDto.setFeb(associateMonthDto.getFeb() + 1);
					} else {
						associateExceptMonthDto.setFeb(associateExceptMonthDto.getFeb() + 1);
					}

				}

				if (!StringUtils.isEmpty(februaryLevel.trim()) && StringUtils.isEmpty(marchLevel.trim())) {

					if (("6G").equals(februaryLevel)) {
						associateMonthDto.setMar(associateMonthDto.getMar() + 1);
					} else {
						associateExceptMonthDto.setMar(associateExceptMonthDto.getMar() + 1);
					}

				}

				if (!StringUtils.isEmpty(marchLevel.trim()) && StringUtils.isEmpty(aprilLevel.trim())) {

					if (("6G").equals(marchLevel)) {
						associateMonthDto.setApr(associateMonthDto.getApr() + 1);
					} else {
						associateExceptMonthDto.setApr(associateExceptMonthDto.getApr() + 1);
					}
				}

				if (!StringUtils.isEmpty(aprilLevel.trim()) && StringUtils.isEmpty(mayLevel.trim())) {

					if (("6G").equals(aprilLevel)) {
						associateMonthDto.setMay(associateMonthDto.getMay() + 1);
					} else {
						associateExceptMonthDto.setMay(associateExceptMonthDto.getMay() + 1);
					}
				}

				if (!StringUtils.isEmpty(mayLevel.trim()) && StringUtils.isEmpty(juneLevel.trim())) {

					if (("6G").equals(mayLevel)) {
						associateMonthDto.setJun(associateMonthDto.getJun() + 1);
					} else {
						associateExceptMonthDto.setJun(associateExceptMonthDto.getJun() + 1);
					}
				}

				if (!StringUtils.isEmpty(juneLevel.trim()) && StringUtils.isEmpty(julyLevel)) {

					if (("6G").equals(juneLevel)) {
						associateMonthDto.setJul(associateMonthDto.getJul() + 1);
					} else {
						associateExceptMonthDto.setJul(associateExceptMonthDto.getJul() + 1);
					}
				}

				if (!StringUtils.isEmpty(julyLevel.trim()) && StringUtils.isEmpty(augustLevel.trim())) {

					if (("6G").equals(julyLevel)) {
						associateMonthDto.setAug(associateMonthDto.getAug() + 1);
					} else {
						associateExceptMonthDto.setAug(associateExceptMonthDto.getAug() + 1);
					}
				}

				if (!StringUtils.isEmpty(augustLevel.trim()) && StringUtils.isEmpty(septemberLevel.trim())) {

					if (("6G").equals(augustLevel)) {
						associateMonthDto.setSep(associateMonthDto.getSep() + 1);
					} else {
						associateExceptMonthDto.setSep(associateExceptMonthDto.getSep() + 1);
					}
				}

				if (!StringUtils.isEmpty(septemberLevel.trim()) && StringUtils.isEmpty(octoberLevel.trim())) {

					if (("6G").equals(septemberLevel)) {
						associateMonthDto.setOct(associateMonthDto.getOct() + 1);
					} else {
						associateExceptMonthDto.setOct(associateExceptMonthDto.getOct() + 1);
					}
				}

				if (!StringUtils.isEmpty(octoberLevel.trim()) && StringUtils.isEmpty(novemberLevel.trim())) {

					if (("6G").equals(octoberLevel)) {
						associateMonthDto.setNov(associateMonthDto.getNov() + 1);
					} else {
						associateExceptMonthDto.setNov(associateExceptMonthDto.getNov() + 1);
					}
				}

				if (!StringUtils.isEmpty(novemberLevel.trim()) && StringUtils.isEmpty(decemberLevel.trim())) {

					if (("6G").equals(novemberLevel)) {
						associateMonthDto.setDec(associateMonthDto.getDec() + 1);
					} else {
						associateExceptMonthDto.setDec(associateExceptMonthDto.getDec() + 1);
					}
				}

			}

			// 1月合计数
			totalMonthDto.setJan(associateMonthDto.getJan() + associateExceptMonthDto.getJan());
			// 2月合计数
			totalMonthDto.setFeb(associateMonthDto.getFeb() + associateExceptMonthDto.getFeb());
			// 3月合计数
			totalMonthDto.setMar(associateMonthDto.getMar() + associateExceptMonthDto.getMar());

			// 1Q associateQuarter1
			associateQuarterDto
					.setQuarter1(associateMonthDto.getJan() + associateMonthDto.getFeb() + associateMonthDto.getMar());
			// 1Q associateExceptQuarter1
			associateExceptQuarterDto.setQuarter1(associateExceptMonthDto.getJan() + associateExceptMonthDto.getFeb()
					+ associateExceptMonthDto.getMar());
			// 1Q Total
			totalQuarterDto.setQuarter1(associateQuarterDto.getQuarter1() + associateExceptQuarterDto.getQuarter1());

			// 4月合计数
			totalMonthDto.setApr(associateMonthDto.getApr() + associateExceptMonthDto.getApr());
			// 5月合计数
			totalMonthDto.setMay(associateMonthDto.getMay() + associateExceptMonthDto.getMay());
			// 6月合计数
			totalMonthDto.setJun(associateMonthDto.getJun() + associateExceptMonthDto.getJun());

			// 2Q associateQuarter2
			associateQuarterDto
					.setQuarter2(associateMonthDto.getApr() + associateMonthDto.getMay() + associateMonthDto.getJun());
			// 2Q associateExceptQuarter2
			associateExceptQuarterDto.setQuarter2(associateExceptMonthDto.getApr() + associateExceptMonthDto.getMay()
					+ associateExceptMonthDto.getJun());
			// 2Q Total
			totalQuarterDto.setQuarter2(associateQuarterDto.getQuarter2() + associateExceptQuarterDto.getQuarter2());

			// 7月合计数
			totalMonthDto.setJul(associateMonthDto.getJul() + associateExceptMonthDto.getJul());
			// 8月合计数
			totalMonthDto.setAug(associateMonthDto.getAug() + associateExceptMonthDto.getAug());
			// 9月合计数
			totalMonthDto.setSep(associateMonthDto.getSep() + associateExceptMonthDto.getSep());

			// 3Q associateQuarter1
			associateQuarterDto
					.setQuarter3(associateMonthDto.getJul() + associateMonthDto.getAug() + associateMonthDto.getSep());
			// 3Q associateExceptQuarter1
			associateExceptQuarterDto.setQuarter3(associateExceptMonthDto.getJul() + associateExceptMonthDto.getAug()
					+ associateExceptMonthDto.getSep());
			// 3Q Total
			totalQuarterDto.setQuarter3(associateQuarterDto.getQuarter3() + associateExceptQuarterDto.getQuarter3());

			// 10月合计数
			totalMonthDto.setOct(associateMonthDto.getOct() + associateExceptMonthDto.getOct());
			// 11月合计数
			totalMonthDto.setNov(associateMonthDto.getNov() + associateExceptMonthDto.getNov());
			// 12月合计数
			totalMonthDto.setDec(associateMonthDto.getDec() + associateExceptMonthDto.getDec());

			// 4Q associateQuarter4
			associateQuarterDto
					.setQuarter4(associateMonthDto.getOct() + associateMonthDto.getNov() + associateMonthDto.getDec());
			// 4Q associateExceptQuarter4
			associateExceptQuarterDto.setQuarter4(associateExceptMonthDto.getOct() + associateExceptMonthDto.getFeb()
					+ associateExceptMonthDto.getDec());
			// 4Q Total
			totalQuarterDto.setQuarter4(associateQuarterDto.getQuarter4() + associateExceptQuarterDto.getQuarter4());

			// 总计人数
			int total = totalMonthDto.getJan() + totalMonthDto.getFeb() + totalMonthDto.getMay()
					+ totalMonthDto.getApr() + totalMonthDto.getMar() + totalMonthDto.getJun() + totalMonthDto.getJul()
					+ totalMonthDto.getAug() + totalMonthDto.getSep() + totalMonthDto.getOct() + totalMonthDto.getNov()
					+ totalMonthDto.getDec();

			associateMonthDto.setTitle("Associate");
			associateExceptMonthDto.setTitle("Associate以外");
			totalMonthDto.setTitle(belongingYear + " 年  Total " + total);

			associateQuarterDto.setTitle("Associate");
			associateExceptQuarterDto.setTitle("Associate以外");
			totalQuarterDto.setTitle(belongingYear + "年  Total " + total);

			// 表１
			List<NewHireMonthDto> listMonthTmp = new ArrayList<>();
			listMonthTmp.add(associateMonthDto);
			listMonthTmp.add(associateExceptMonthDto);
			listMonthTmp.add(totalMonthDto);

			// 表２
			List<NewHireQuarterDto> ListQuarterTmp = new ArrayList<>();
			ListQuarterTmp.add(associateQuarterDto);
			ListQuarterTmp.add(associateExceptQuarterDto);
			ListQuarterTmp.add(totalQuarterDto);

			newHireDto.setListMonth(listMonthTmp);
			newHireDto.setListQuarter(ListQuarterTmp);

			// 返回加工好的检索数据
			return Result.success(newHireDto);
		}

		// 检索结果不存在的情况，返回不存在的信息
		return Result.error("检索结果零件，请重新设定检索条件。");

	}

	/**
	 * 
	 * 晋升人员的统计信息
	 * 
	 * @param userToken     token
	 * @param belongingYear 画面所在年入力的年份
	 * @return 月/季的晋升人员的统计信息
	 * 
	 */
	public Result getPromotionSearch(String authorization, String belongingYear) {

		// token取得
		UserToken userToken = userTokenRepository.findByTokenId(authorization);

		List<Object[]> promotionList = bandmixInfoRepository
				.findByBelongingTowerCodeAndBelongingYear(userToken.getOrganizationAuthorityCode(), belongingYear);

		// 返回结果存在且不为空
		if (promotionList != null && promotionList.size() > 0) {

			PromotionDto promotionDto = new PromotionDto();

			// DB中等级为空时
			String BandIsEmpty = "-";

			// 遍历list
			for (Object[] bandmixPromotioInfo : promotionList) {

				// 去年12月级别
				String beforyersdecember = (String) bandmixPromotioInfo[0];
				if (BandIsEmpty.equals(beforyersdecember)) {
					beforyersdecember = "";
				}
				// 1月级别
				String januaryLevel = (String) bandmixPromotioInfo[1];
				if (BandIsEmpty.equals(januaryLevel)) {
					januaryLevel = "";
				}
				// 2月级别
				String februaryLevel = (String) bandmixPromotioInfo[2];
				if (BandIsEmpty.equals(februaryLevel)) {
					februaryLevel = "";
				}
				// 3月级别
				String marchLevel = (String) bandmixPromotioInfo[3];
				if (BandIsEmpty.equals(marchLevel)) {
					marchLevel = "";
				}
				// 4月级别
				String aprilLevel = (String) bandmixPromotioInfo[4];
				if (BandIsEmpty.equals(aprilLevel)) {
					aprilLevel = "";
				}
				// 5月级别
				String mayLevel = (String) bandmixPromotioInfo[5];
				if (BandIsEmpty.equals(mayLevel)) {
					mayLevel = "";
				}
				// 6月级别
				String juneLevel = (String) bandmixPromotioInfo[6];
				if (BandIsEmpty.equals(juneLevel)) {
					juneLevel = "";
				}
				// 7月级别
				String julyLevel = (String) bandmixPromotioInfo[7];
				if (BandIsEmpty.equals(julyLevel)) {
					julyLevel = "";
				}
				// 8月级别
				String augustLevel = (String) bandmixPromotioInfo[8];
				if (BandIsEmpty.equals(augustLevel)) {
					augustLevel = "";
				}
				// 9月级别
				String septemberLevel = (String) bandmixPromotioInfo[9];
				if (BandIsEmpty.equals(septemberLevel)) {
					septemberLevel = "";
				}
				// 10月级别
				String octoberLevel = (String) bandmixPromotioInfo[10];
				if (BandIsEmpty.equals(octoberLevel)) {
					octoberLevel = "";
				}
				// 11月级别
				String novemberLevel = (String) bandmixPromotioInfo[11];
				if (BandIsEmpty.equals(novemberLevel)) {
					novemberLevel = "";
				}
				// 12月级别
				String decemberLevel = (String) bandmixPromotioInfo[12];
				if (BandIsEmpty.equals(decemberLevel)) {
					decemberLevel = "";
				}

				// 去年12月级别和1月级别不为空且不相等
				if (beforyersdecember != null && !beforyersdecember.isEmpty() && !januaryLevel.isEmpty()
						&& !beforyersdecember.equals(januaryLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 1, januaryLevel);

				}

				// 1月级别和2月级别不为空且不相等
				if (!januaryLevel.isEmpty() && !februaryLevel.isEmpty() && !januaryLevel.equals(februaryLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 2, februaryLevel);

				}

				// 2月级别和3月级别不为空且不相等
				if (!februaryLevel.isEmpty() && !marchLevel.isEmpty() && !februaryLevel.equals(marchLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 3, marchLevel);

				}

				// 3月级别和4月级别不为空且不相等
				if (!marchLevel.isEmpty() && !aprilLevel.isEmpty() && !marchLevel.equals(aprilLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 4, aprilLevel);

				}

				// 4月级别和5月级别不为空且不相等
				if (!aprilLevel.isEmpty() && !mayLevel.isEmpty() && !aprilLevel.equals(mayLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 5, mayLevel);

				}

				// 5月级别和6月级别不为空且不相等
				if (!mayLevel.isEmpty() && !juneLevel.isEmpty() && !mayLevel.equals(juneLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 6, juneLevel);

				}

				// 6月级别和7月级别不为空且不相等
				if (!juneLevel.isEmpty() && !julyLevel.isEmpty() && !juneLevel.equals(julyLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 7, julyLevel);

				}

				// 7月级别和8月级别不为空且不相等
				if (!julyLevel.isEmpty() && !augustLevel.isEmpty() && !julyLevel.equals(augustLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 8, augustLevel);

				}

				// 8月级别和9月级别不为空且不相等
				if (!augustLevel.isEmpty() && !septemberLevel.isEmpty() && !augustLevel.equals(septemberLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 9, septemberLevel);

				}

				// 9月级别和10月级别不为空且不相等
				if (!septemberLevel.isEmpty() && !octoberLevel.isEmpty() && !septemberLevel.equals(octoberLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 10, octoberLevel);

				}

				// 10月级别和11月级别不为空且不相等
				if (!octoberLevel.isEmpty() && !novemberLevel.isEmpty() && !octoberLevel.equals(novemberLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 11, novemberLevel);

				}

				// 11月级别和12月级别不为空且不相等
				if (!novemberLevel.isEmpty() && !decemberLevel.isEmpty() && !novemberLevel.equals(decemberLevel)) {

					promotionDto = PromotionMonthAndLevel(promotionDto, 12, decemberLevel);

				}

			}

			// 1月合计数
			promotionDto.setSumJan(promotionDto.getB6AJan() + promotionDto.getB6BJan() + promotionDto.getB7AJan()
					+ promotionDto.getB7BJan() + promotionDto.getB8Jan() + promotionDto.getB9Jan());
			// 2月合计数
			promotionDto.setSumFeb(promotionDto.getB6AFeb() + promotionDto.getB6BFeb() + promotionDto.getB7AFeb()
					+ promotionDto.getB7BFeb() + promotionDto.getB8Feb() + promotionDto.getB9Feb());
			// 3月合计数
			promotionDto.setSumMar(promotionDto.getB6AMar() + promotionDto.getB6BMar() + promotionDto.getB7AMar()
					+ promotionDto.getB7BMar() + promotionDto.getB8Mar() + promotionDto.getB9Mar());
			// 4月合计数
			promotionDto.setSumApr(promotionDto.getB6AApr() + promotionDto.getB6BApr() + promotionDto.getB7AApr()
					+ promotionDto.getB7BApr() + promotionDto.getB8Apr() + promotionDto.getB9Apr());
			// 5月合计数
			promotionDto.setSumMay(promotionDto.getB6AMay() + promotionDto.getB6BMay() + promotionDto.getB7AMay()
					+ promotionDto.getB7BMay() + promotionDto.getB8May() + promotionDto.getB9May());
			// 6月合计数
			promotionDto.setSumJun(promotionDto.getB6AJun() + promotionDto.getB6BJun() + promotionDto.getB7AJun()
					+ promotionDto.getB7BJun() + promotionDto.getB8Jun() + promotionDto.getB9Jun());
			// 7月合计数
			promotionDto.setSumJul(promotionDto.getB6AJul() + promotionDto.getB6BJul() + promotionDto.getB7AJul()
					+ promotionDto.getB7BJul() + promotionDto.getB8Jul() + promotionDto.getB9Jul());
			// 8月合计数
			promotionDto.setSumAug(promotionDto.getB6AAug() + promotionDto.getB6BAug() + promotionDto.getB7AAug()
					+ promotionDto.getB7BAug() + promotionDto.getB8Aug() + promotionDto.getB9Aug());
			// 9月合计数
			promotionDto.setSumSep(promotionDto.getB6ASep() + promotionDto.getB6BSep() + promotionDto.getB7ASep()
					+ promotionDto.getB7BSep() + promotionDto.getB8Sep() + promotionDto.getB9Sep());
			// 10月合计数
			promotionDto.setSumOct(promotionDto.getB6AOct() + promotionDto.getB6BOct() + promotionDto.getB7AOct()
					+ promotionDto.getB7BOct() + promotionDto.getB8Oct() + promotionDto.getB9Oct());
			// 11月合计数
			promotionDto.setSumNov(promotionDto.getB6ANov() + promotionDto.getB6BNov() + promotionDto.getB7ANov()
					+ promotionDto.getB7BNov() + promotionDto.getB8Nov() + promotionDto.getB9Nov());
			// 12月合计数
			promotionDto.setSumDec(promotionDto.getB6ADec() + promotionDto.getB6BDec() + promotionDto.getB7ADec()
					+ promotionDto.getB7BDec() + promotionDto.getB8Dec() + promotionDto.getB9Dec());

			// 1Q升6A数
			promotionDto.setB6A1Q(promotionDto.getB6AJan() + promotionDto.getB6AFeb() + promotionDto.getB6AMar());
			// 1Q升6B数
			promotionDto.setB6B1Q(promotionDto.getB6BJan() + promotionDto.getB6BFeb() + promotionDto.getB6BMar());
			// 1Q升7A数
			promotionDto.setB7A1Q(promotionDto.getB7AJan() + promotionDto.getB7AFeb() + promotionDto.getB7AMar());
			// 1Q升7B数
			promotionDto.setB7B1Q(promotionDto.getB7BJan() + promotionDto.getB7BFeb() + promotionDto.getB7BMar());
			// 1Q升8数
			promotionDto.setB81Q(promotionDto.getB8Jan() + promotionDto.getB8Feb() + promotionDto.getB8Mar());
			// 1Q升9数
			promotionDto.setB91Q(promotionDto.getB9Jan() + promotionDto.getB9Feb() + promotionDto.getB9Mar());

			// 2Q升6A数
			promotionDto.setB6A2Q(promotionDto.getB6AApr() + promotionDto.getB6AMay() + promotionDto.getB6AJun());
			// 2Q升6B数
			promotionDto.setB6B2Q(promotionDto.getB6BApr() + promotionDto.getB6BMay() + promotionDto.getB6BJun());
			// 2Q升7A数
			promotionDto.setB7A2Q(promotionDto.getB7AApr() + promotionDto.getB7AMay() + promotionDto.getB7AJun());
			// 2Q升7B数
			promotionDto.setB7B2Q(promotionDto.getB7BApr() + promotionDto.getB7BMay() + promotionDto.getB7BJun());
			// 2Q升8数
			promotionDto.setB82Q(promotionDto.getB8Apr() + promotionDto.getB8May() + promotionDto.getB8Jun());
			// 2Q升9数
			promotionDto.setB92Q(promotionDto.getB9Apr() + promotionDto.getB9May() + promotionDto.getB9Jun());

			// 3Q升6A数
			promotionDto.setB6A3Q(promotionDto.getB6AJul() + promotionDto.getB6AAug() + promotionDto.getB6ASep());
			// 3Q升6B数
			promotionDto.setB6B3Q(promotionDto.getB6BJul() + promotionDto.getB6BAug() + promotionDto.getB6BSep());
			// 3Q升7A数
			promotionDto.setB7A3Q(promotionDto.getB7AJul() + promotionDto.getB7AAug() + promotionDto.getB7ASep());
			// 3Q升7B数
			promotionDto.setB7B3Q(promotionDto.getB7BJul() + promotionDto.getB7BAug() + promotionDto.getB7BSep());
			// 3Q升8数
			promotionDto.setB83Q(promotionDto.getB8Jul() + promotionDto.getB8Aug() + promotionDto.getB8Sep());
			// 3Q升9数
			promotionDto.setB93Q(promotionDto.getB9Jul() + promotionDto.getB9Aug() + promotionDto.getB9Sep());

			// 4Q升6A数
			promotionDto.setB6A4Q(promotionDto.getB6AOct() + promotionDto.getB6ANov() + promotionDto.getB6ADec());
			// 4Q升6B数
			promotionDto.setB6B4Q(promotionDto.getB6BOct() + promotionDto.getB6BNov() + promotionDto.getB6BDec());
			// 4Q升7A数
			promotionDto.setB7A4Q(promotionDto.getB7AOct() + promotionDto.getB7ANov() + promotionDto.getB7ADec());
			// 4Q升7B数
			promotionDto.setB7B4Q(promotionDto.getB7BOct() + promotionDto.getB7BNov() + promotionDto.getB7BDec());
			// 4Q升8数
			promotionDto.setB84Q(promotionDto.getB8Oct() + promotionDto.getB8Nov() + promotionDto.getB8Dec());
			// 4Q升9数
			promotionDto.setB94Q(promotionDto.getB9Oct() + promotionDto.getB9Nov() + promotionDto.getB9Dec());

			// 1Q合计数
			promotionDto.setSum1Q(promotionDto.getB6A1Q() + promotionDto.getB6B1Q() + promotionDto.getB7A1Q()
					+ promotionDto.getB7B1Q() + promotionDto.getB81Q() + promotionDto.getB91Q());
			// 2Q合计数
			promotionDto.setSum2Q(promotionDto.getB6A2Q() + promotionDto.getB6B2Q() + promotionDto.getB7A2Q()
					+ promotionDto.getB7B2Q() + promotionDto.getB82Q() + promotionDto.getB92Q());
			// 3Q合计数
			promotionDto.setSum3Q(promotionDto.getB6A3Q() + promotionDto.getB6B3Q() + promotionDto.getB7A3Q()
					+ promotionDto.getB7B3Q() + promotionDto.getB83Q() + promotionDto.getB93Q());
			// 4Q合计数
			promotionDto.setSum4Q(promotionDto.getB6A4Q() + promotionDto.getB6B4Q() + promotionDto.getB7A4Q()
					+ promotionDto.getB7B4Q() + promotionDto.getB84Q() + promotionDto.getB94Q());

			// 年度合计数
			promotionDto.setSumYear(promotionDto.getSumJan() + promotionDto.getSumFeb() + promotionDto.getSumMar()
					+ promotionDto.getSumApr() + promotionDto.getSumMay() + promotionDto.getSumJun()
					+ promotionDto.getSumJul() + promotionDto.getSumAug() + promotionDto.getSumSep()
					+ promotionDto.getSumOct() + promotionDto.getSumNov() + promotionDto.getSumDec());

			// 1月至12月的6G升6A人数
			PromotionMonthListDto promotionMonthB6a = new PromotionMonthListDto();
			promotionMonthB6a.setTitle("6G -> 6A");
			promotionMonthB6a.setJan(promotionDto.getB6AJan());
			promotionMonthB6a.setFeb(promotionDto.getB6AFeb());
			promotionMonthB6a.setMar(promotionDto.getB6AMar());
			promotionMonthB6a.setApr(promotionDto.getB6AApr());
			promotionMonthB6a.setMay(promotionDto.getB6AMay());
			promotionMonthB6a.setJun(promotionDto.getB6AJun());
			promotionMonthB6a.setJul(promotionDto.getB6AJul());
			promotionMonthB6a.setAug(promotionDto.getB6AAug());
			promotionMonthB6a.setSep(promotionDto.getB6ASep());
			promotionMonthB6a.setOct(promotionDto.getB6AOct());
			promotionMonthB6a.setNov(promotionDto.getB6ANov());
			promotionMonthB6a.setDec(promotionDto.getB6ADec());

			// 1月至12月的6A升6B人数
			PromotionMonthListDto promotionMonthB6b = new PromotionMonthListDto();
			promotionMonthB6b.setTitle("6A -> 6B");
			promotionMonthB6b.setJan(promotionDto.getB6BJan());
			promotionMonthB6b.setFeb(promotionDto.getB6BFeb());
			promotionMonthB6b.setMar(promotionDto.getB6BMar());
			promotionMonthB6b.setApr(promotionDto.getB6BApr());
			promotionMonthB6b.setMay(promotionDto.getB6BMay());
			promotionMonthB6b.setJun(promotionDto.getB6BJun());
			promotionMonthB6b.setJul(promotionDto.getB6BJul());
			promotionMonthB6b.setAug(promotionDto.getB6BAug());
			promotionMonthB6b.setSep(promotionDto.getB6BSep());
			promotionMonthB6b.setOct(promotionDto.getB6BOct());
			promotionMonthB6b.setNov(promotionDto.getB6BNov());
			promotionMonthB6b.setDec(promotionDto.getB6BDec());

			// 1月至12月的6B升7A人数
			PromotionMonthListDto promotionMonthB7a = new PromotionMonthListDto();
			promotionMonthB7a.setTitle("6B -> 7A");
			promotionMonthB7a.setJan(promotionDto.getB7AJan());
			promotionMonthB7a.setFeb(promotionDto.getB7AFeb());
			promotionMonthB7a.setMar(promotionDto.getB7AMar());
			promotionMonthB7a.setApr(promotionDto.getB7AApr());
			promotionMonthB7a.setMay(promotionDto.getB7AMay());
			promotionMonthB7a.setJun(promotionDto.getB7AJun());
			promotionMonthB7a.setJul(promotionDto.getB7AJul());
			promotionMonthB7a.setAug(promotionDto.getB7AAug());
			promotionMonthB7a.setSep(promotionDto.getB7ASep());
			promotionMonthB7a.setOct(promotionDto.getB7AOct());
			promotionMonthB7a.setNov(promotionDto.getB7ANov());
			promotionMonthB7a.setDec(promotionDto.getB7ADec());

			// 1月至12月的7A升7B人数
			PromotionMonthListDto promotionMonthB7b = new PromotionMonthListDto();
			promotionMonthB7b.setTitle("7A -> 7B");
			promotionMonthB7b.setJan(promotionDto.getB7BJan());
			promotionMonthB7b.setFeb(promotionDto.getB7BFeb());
			promotionMonthB7b.setMar(promotionDto.getB7BMar());
			promotionMonthB7b.setApr(promotionDto.getB7BApr());
			promotionMonthB7b.setMay(promotionDto.getB7BMay());
			promotionMonthB7b.setJun(promotionDto.getB7BJun());
			promotionMonthB7b.setJul(promotionDto.getB7BJul());
			promotionMonthB7b.setAug(promotionDto.getB7BAug());
			promotionMonthB7b.setSep(promotionDto.getB7BSep());
			promotionMonthB7b.setOct(promotionDto.getB7BOct());
			promotionMonthB7b.setNov(promotionDto.getB7BNov());
			promotionMonthB7b.setDec(promotionDto.getB7BDec());

			// 1月至12月的7B升8人数
			PromotionMonthListDto promotionMonthB8 = new PromotionMonthListDto();
			promotionMonthB8.setTitle("7B -> 8");
			promotionMonthB8.setJan(promotionDto.getB8Jan());
			promotionMonthB8.setFeb(promotionDto.getB8Feb());
			promotionMonthB8.setMar(promotionDto.getB8Mar());
			promotionMonthB8.setApr(promotionDto.getB8Apr());
			promotionMonthB8.setMay(promotionDto.getB8May());
			promotionMonthB8.setJun(promotionDto.getB8Jun());
			promotionMonthB8.setJul(promotionDto.getB8Jul());
			promotionMonthB8.setAug(promotionDto.getB8Aug());
			promotionMonthB8.setSep(promotionDto.getB8Sep());
			promotionMonthB8.setOct(promotionDto.getB8Oct());
			promotionMonthB8.setNov(promotionDto.getB8Nov());
			promotionMonthB8.setDec(promotionDto.getB8Dec());

			// 1月至12月的8升9人数
			PromotionMonthListDto promotionMonthB9 = new PromotionMonthListDto();
			promotionMonthB9.setTitle("8 -> 9");
			promotionMonthB9.setJan(promotionDto.getB9Jan());
			promotionMonthB9.setFeb(promotionDto.getB9Feb());
			promotionMonthB9.setMar(promotionDto.getB9Mar());
			promotionMonthB9.setApr(promotionDto.getB9Apr());
			promotionMonthB9.setMay(promotionDto.getB9May());
			promotionMonthB9.setJun(promotionDto.getB9Jun());
			promotionMonthB9.setJul(promotionDto.getB9Jul());
			promotionMonthB9.setAug(promotionDto.getB9Aug());
			promotionMonthB9.setSep(promotionDto.getB9Sep());
			promotionMonthB9.setOct(promotionDto.getB9Oct());
			promotionMonthB9.setNov(promotionDto.getB9Nov());
			promotionMonthB9.setDec(promotionDto.getB9Dec());

			// 1月至12月的总升band人数
			PromotionMonthListDto promotionMonthSum = new PromotionMonthListDto();
			promotionMonthSum.setTitle(belongingYear + " Total:" + promotionDto.getSumYear());
			promotionMonthSum.setJan(promotionDto.getSumJan());
			promotionMonthSum.setFeb(promotionDto.getSumFeb());
			promotionMonthSum.setMar(promotionDto.getSumMar());
			promotionMonthSum.setApr(promotionDto.getSumApr());
			promotionMonthSum.setMay(promotionDto.getSumMay());
			promotionMonthSum.setJun(promotionDto.getSumJun());
			promotionMonthSum.setJul(promotionDto.getSumJul());
			promotionMonthSum.setAug(promotionDto.getSumAug());
			promotionMonthSum.setSep(promotionDto.getSumSep());
			promotionMonthSum.setOct(promotionDto.getSumOct());
			promotionMonthSum.setNov(promotionDto.getSumNov());
			promotionMonthSum.setDec(promotionDto.getSumDec());

			// 1Q至4Q的6G升6A人数
			PromotionQuarterListDto promotionQuarterB6a = new PromotionQuarterListDto();
			promotionQuarterB6a.setTitle("6G -> 6A");
			promotionQuarterB6a.setQuarter1(promotionDto.getB6A1Q());
			promotionQuarterB6a.setQuarter2(promotionDto.getB6A2Q());
			promotionQuarterB6a.setQuarter3(promotionDto.getB6A3Q());
			promotionQuarterB6a.setQuarter4(promotionDto.getB6A4Q());

			// 1Q至4Q的6A升6B人数
			PromotionQuarterListDto promotionQuarterB6b = new PromotionQuarterListDto();
			promotionQuarterB6b.setTitle("6A -> 6B");
			promotionQuarterB6b.setQuarter1(promotionDto.getB6B1Q());
			promotionQuarterB6b.setQuarter2(promotionDto.getB6B2Q());
			promotionQuarterB6b.setQuarter3(promotionDto.getB6B3Q());
			promotionQuarterB6b.setQuarter4(promotionDto.getB6B4Q());

			// 1Q至4Q的6B升7A人数
			PromotionQuarterListDto promotionQuarterB7a = new PromotionQuarterListDto();
			promotionQuarterB7a.setTitle("6B -> 7A");
			promotionQuarterB7a.setQuarter1(promotionDto.getB7A1Q());
			promotionQuarterB7a.setQuarter2(promotionDto.getB7A2Q());
			promotionQuarterB7a.setQuarter3(promotionDto.getB7A3Q());
			promotionQuarterB7a.setQuarter4(promotionDto.getB7A4Q());

			// 1Q至4Q的7A升7B人数
			PromotionQuarterListDto promotionQuarterB7b = new PromotionQuarterListDto();
			promotionQuarterB7b.setTitle("7A -> 7B");
			promotionQuarterB7b.setQuarter1(promotionDto.getB7B1Q());
			promotionQuarterB7b.setQuarter2(promotionDto.getB7B2Q());
			promotionQuarterB7b.setQuarter3(promotionDto.getB7B3Q());
			promotionQuarterB7b.setQuarter4(promotionDto.getB7B4Q());

			// 1Q至4Q的7B升8人数
			PromotionQuarterListDto promotionQuarterB8 = new PromotionQuarterListDto();
			promotionQuarterB8.setTitle("7B -> 8");
			promotionQuarterB8.setQuarter1(promotionDto.getB81Q());
			promotionQuarterB8.setQuarter2(promotionDto.getB82Q());
			promotionQuarterB8.setQuarter3(promotionDto.getB83Q());
			promotionQuarterB8.setQuarter4(promotionDto.getB84Q());

			// 1Q至4Q的8升9人数
			PromotionQuarterListDto promotionQuarterB9 = new PromotionQuarterListDto();
			promotionQuarterB9.setTitle("8 -> 9");
			promotionQuarterB9.setQuarter1(promotionDto.getB91Q());
			promotionQuarterB9.setQuarter2(promotionDto.getB92Q());
			promotionQuarterB9.setQuarter3(promotionDto.getB93Q());
			promotionQuarterB9.setQuarter4(promotionDto.getB94Q());

			// 1Q至4Q的总升band人数
			PromotionQuarterListDto promotionQuarterSum = new PromotionQuarterListDto();
			promotionQuarterSum.setTitle(belongingYear + " Total:" + promotionDto.getSumYear());
			promotionQuarterSum.setQuarter1(promotionDto.getSum1Q());
			promotionQuarterSum.setQuarter2(promotionDto.getSum2Q());
			promotionQuarterSum.setQuarter3(promotionDto.getSum3Q());
			promotionQuarterSum.setQuarter4(promotionDto.getSum4Q());

			// 月度list
			List<PromotionMonthListDto> PromotionDtoMonthList = new ArrayList<PromotionMonthListDto>();
			PromotionDtoMonthList.add(promotionMonthB6a);
			PromotionDtoMonthList.add(promotionMonthB6b);
			PromotionDtoMonthList.add(promotionMonthB7a);
			PromotionDtoMonthList.add(promotionMonthB7b);
			PromotionDtoMonthList.add(promotionMonthB8);
			PromotionDtoMonthList.add(promotionMonthB9);
			PromotionDtoMonthList.add(promotionMonthSum);

			// 季度list
			List<PromotionQuarterListDto> PromotionDtoQuarterList = new ArrayList<PromotionQuarterListDto>();
			PromotionDtoQuarterList.add(promotionQuarterB6a);
			PromotionDtoQuarterList.add(promotionQuarterB6b);
			PromotionDtoQuarterList.add(promotionQuarterB7a);
			PromotionDtoQuarterList.add(promotionQuarterB7b);
			PromotionDtoQuarterList.add(promotionQuarterB8);
			PromotionDtoQuarterList.add(promotionQuarterB9);
			PromotionDtoQuarterList.add(promotionQuarterSum);

			PromotionListDto promotionListDto = new PromotionListDto();

			// 将月度的list集合放入promotionListDto中
			promotionListDto.setPromotionDtoMonthList(PromotionDtoMonthList);
			// 将季度的list集合放入promotionListDto中
			promotionListDto.setPromotionDtoQuarterList(PromotionDtoQuarterList);

			// 返回升值人数List
			return Result.success(promotionListDto);

		}
		// 所在年份不存在，返回error message
		return Result.error("检索结果零件，请重新设定检索条件。");

	}

	/**
	 * 
	 * 比率计算（Promotion ratio(All) / Promotion ratio(WithoutGH) / GH Hire ratio / New
	 * Hire ratio / Resign ratio）
	 * 
	 * @param towerCode     用户信息的所属组织Code
	 * @param belongingYear 画面所在年选择的年份
	 * @return 对应查询比率类型的计算结果
	 * 
	 */
	public Result getRatioStatics(String authorization, String belongingYear) {

		// token取得
		UserToken userToken = userTokenRepository.findByTokenId(authorization);
			
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			// 当前年份的前一年
			int lastYear = year - 1;
			// 所在年份型转换
			int getbelongingYear = Integer.parseInt(belongingYear);

			// 所在年份 ≤ 当前年份的前一年
			if (getbelongingYear <= lastYear) {
				
				List<Object[]> promotionList = bandmixInfoRepository.findByBelongingTowerCodeAndBelongingYear(userToken.getOrganizationAuthorityCode(),
						belongingYear);

				// 返回结果存在且不为空
				if (promotionList != null && promotionList.size() > 0) {
					
				RatioDto ratioDto = new RatioDto();

				// 晋升人数
				int PromotionNumber = 0;
				// 晋升GH人数
				int PromotionNumberGh = 0;
				// 新入职GH人数
				int GhHireNumber = 0;
				// 新入职员工人数
				int NewHireNumber = 0;

				// DB中等级为空时
				String BandIsEmpty = "-";

				// 遍历list
				for (Object[] bandmixPromotioInfo : promotionList) {

					// 去年12月级别
					String beforyersdecember = (String) bandmixPromotioInfo[0];
					if (BandIsEmpty.equals(beforyersdecember)) {
						beforyersdecember = "";
					}
					// 1月级别
					String januaryLevel = (String) bandmixPromotioInfo[1];
					if (BandIsEmpty.equals(januaryLevel)) {
						januaryLevel = "";
					}
					// 2月级别
					String februaryLevel = (String) bandmixPromotioInfo[2];
					if (BandIsEmpty.equals(februaryLevel)) {
						februaryLevel = "";
					}
					// 3月级别
					String marchLevel = (String) bandmixPromotioInfo[3];
					if (BandIsEmpty.equals(marchLevel)) {
						marchLevel = "";
					}
					// 4月级别
					String aprilLevel = (String) bandmixPromotioInfo[4];
					if (BandIsEmpty.equals(aprilLevel)) {
						aprilLevel = "";
					}
					// 5月级别
					String mayLevel = (String) bandmixPromotioInfo[5];
					if (BandIsEmpty.equals(mayLevel)) {
						mayLevel = "";
					}
					// 6月级别
					String juneLevel = (String) bandmixPromotioInfo[6];
					if (BandIsEmpty.equals(juneLevel)) {
						juneLevel = "";
					}
					// 7月级别
					String julyLevel = (String) bandmixPromotioInfo[7];
					if (BandIsEmpty.equals(julyLevel)) {
						julyLevel = "";
					}
					// 8月级别
					String augustLevel = (String) bandmixPromotioInfo[8];
					if (BandIsEmpty.equals(augustLevel)) {
						augustLevel = "";
					}
					// 9月级别
					String septemberLevel = (String) bandmixPromotioInfo[9];
					if (BandIsEmpty.equals(septemberLevel)) {
						septemberLevel = "";
					}
					// 10月级别
					String octoberLevel = (String) bandmixPromotioInfo[10];
					if (BandIsEmpty.equals(octoberLevel)) {
						octoberLevel = "";
					}
					// 11月级别
					String novemberLevel = (String) bandmixPromotioInfo[11];
					if (BandIsEmpty.equals(novemberLevel)) {
						novemberLevel = "";
					}
					// 12月级别
					String decemberLevel = (String) bandmixPromotioInfo[12];
					if (BandIsEmpty.equals(decemberLevel)) {
						decemberLevel = "";
					}
					// 去年12月级别不为空
					if (beforyersdecember != null && !beforyersdecember.isEmpty()) {

						// 1月级别不为空且去年12月级别和1月级别不相等
						if (!januaryLevel.isEmpty() && !beforyersdecember.equals(januaryLevel)) {

							// 去年12月级别为6G且1月级别为6A（6G->6A）
							if (("6G").equals(beforyersdecember) && ("6A").equals(januaryLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					}

					// 1月级别不为空
					if (januaryLevel != null && !januaryLevel.isEmpty()) {

						// 2月级别不为空且1月级别和2月级别不相等
						if (!februaryLevel.isEmpty() && !januaryLevel.equals(februaryLevel)) {

							// 1月级别为6G且2月级别为6A（6G->6A）
							if (("6G").equals(januaryLevel) && ("6A").equals(februaryLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 2月级别不为空
						if (!februaryLevel.isEmpty()) {

							// 2月级别为6G
							if (("6G").equals(februaryLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 2月级别不为空
					if (februaryLevel != null && !februaryLevel.isEmpty()) {

						// 3月级别不为空且2月级别和3月级别不相等
						if (!marchLevel.isEmpty() && !februaryLevel.equals(marchLevel)) {

							// 2月级别为6G且3月级别为6A（6G->6A）
							if (("6G").equals(februaryLevel) && ("6A").equals(marchLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 3月级别不为空
						if (!marchLevel.isEmpty()) {

							// 3月级别为6G
							if (("6G").equals(marchLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 3月级别不为空
					if (marchLevel != null && !marchLevel.isEmpty()) {

						// 4月级别不为空且3月级别和4月级别不相等
						if (!aprilLevel.isEmpty() && !marchLevel.equals(aprilLevel)) {

							// 3月级别为6G且4月级别为6A（6G->6A）
							if (("6G").equals(marchLevel) && ("6A").equals(aprilLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 4月级别不为空
						if (!aprilLevel.isEmpty()) {

							// 4月级别为6G
							if (("6G").equals(aprilLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 4月级别不为空
					if (aprilLevel != null && !aprilLevel.isEmpty()) {

						// 5月级别不为空且4月级别和5月级别不相等
						if (!mayLevel.isEmpty() && !aprilLevel.equals(mayLevel)) {

							// 4月级别为6G且5月级别为6A（6G->6A）
							if (("6G").equals(aprilLevel) && ("6A").equals(mayLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 5月级别不为空
						if (!mayLevel.isEmpty()) {

							// 5月级别为6G
							if (("6G").equals(mayLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 5月级别不为空
					if (mayLevel != null && !mayLevel.isEmpty()) {

						// 6月级别不为空且5月级别和6月级别不相等
						if (!juneLevel.isEmpty() && !mayLevel.equals(juneLevel)) {

							// 5月级别为6G且6月级别为6A（6G->6A）
							if (("6G").equals(mayLevel) && ("6A").equals(juneLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 6月级别不为空
						if (!juneLevel.isEmpty()) {

							// 6月级别为6G
							if (("6G").equals(juneLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 6月级别不为空
					if (juneLevel != null && !juneLevel.isEmpty()) {

						// 7月级别不为空且6月级别和7月级别不相等
						if (!julyLevel.isEmpty() && !juneLevel.equals(julyLevel)) {

							// 6月级别为6G且7月级别为6A（6G->6A）
							if (("6G").equals(juneLevel) && ("6A").equals(julyLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 7月级别不为空
						if (!julyLevel.isEmpty()) {

							// 7月级别为6G
							if (("6G").equals(julyLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 7月级别不为空
					if (julyLevel != null && !julyLevel.isEmpty()) {

						// 8月级别不为空且7月级别和8月级别不相等
						if (!augustLevel.isEmpty() && !julyLevel.equals(augustLevel)) {

							// 7月级别为6G且8月级别为6A（6G->6A）
							if (("6G").equals(julyLevel) && ("6A").equals(augustLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 8月级别不为空
						if (!augustLevel.isEmpty()) {

							// 8月级别为6G
							if (("6G").equals(augustLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 8月级别不为空
					if (augustLevel != null && !augustLevel.isEmpty()) {

						// 9月级别不为空且8月级别和9月级别不相等
						if (!septemberLevel.isEmpty() && !augustLevel.equals(septemberLevel)) {

							// 8月级别为6G且9月级别为6A（6G->6A）
							if (("6G").equals(augustLevel) && ("6A").equals(septemberLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 9月级别不为空
						if (!septemberLevel.isEmpty()) {

							// 9月级别为6G
							if (("6G").equals(septemberLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 9月级别不为空
					if (septemberLevel != null && !septemberLevel.isEmpty()) {

						// 10月级别不为空且9月级别和10月级别不相等
						if (!octoberLevel.isEmpty() && !septemberLevel.equals(octoberLevel)) {

							// 9月级别为6G且10月级别为6A（6G->6A）
							if (("6G").equals(septemberLevel) && ("6A").equals(octoberLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 10月级别不为空
						if (!octoberLevel.isEmpty()) {

							// 10月级别为6G
							if (("6G").equals(octoberLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 10月级别不为空
					if (octoberLevel != null && !octoberLevel.isEmpty()) {

						// 11月级别不为空且10月级别和11月级别不相等
						if (!novemberLevel.isEmpty() && !octoberLevel.equals(novemberLevel)) {

							// 10月级别为6G且11月级别为6A（6G->6A）
							if (("6G").equals(octoberLevel) && ("6A").equals(novemberLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 11月级别不为空
						if (!novemberLevel.isEmpty()) {

							// 11月级别为6G
							if (("6G").equals(novemberLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

					// 11月级别不为空
					if (novemberLevel != null && !novemberLevel.isEmpty()) {

						// 12月级别不为空且11月级别和12月级别不相等
						if (!decemberLevel.isEmpty() && !novemberLevel.equals(decemberLevel)) {

							// 11月级别为6G且12月级别为6A（6G->6A）
							if (("6G").equals(novemberLevel) && ("6A").equals(decemberLevel)) {

								// 晋升GH人数 + 1
								PromotionNumberGh = PromotionNumberGh + 1;

							}

							// 晋升人数 + 1
							PromotionNumber = PromotionNumber + 1;

						}

					} else {

						// 12月级别不为空
						if (!decemberLevel.isEmpty()) {

							// 12月级别为6G
							if (("6G").equals(decemberLevel)) {

								// 新入职GH + 1
								GhHireNumber = GhHireNumber + 1;

							}

							// 新入职员工 + 1
							NewHireNumber = NewHireNumber + 1;

						}

					}

				}

				// 查询所在年份1月的总人数
				int belongingYearFirstMonthCount = bandmixInfoRepository.findByBelongingYearFirstMonthCount(userToken.getOrganizationAuthorityCode(),
						belongingYear);
				// 查询所在年份12月的总人数
				int belongingYearLastMonthCount = bandmixInfoRepository.findByBelongingYearLastMonthCount(userToken.getOrganizationAuthorityCode(),
						belongingYear);

				// 晋升人数（GH除外）
				int PromotionNumberWithoutGH = PromotionNumber - PromotionNumberGh;

				// 转换为浮点型
				float lastMonthCount = (float) belongingYearLastMonthCount;
				float firstMonthCount = (float) belongingYearFirstMonthCount;
				float PromotionNumberCount = (float) PromotionNumber;
				float PromotionNumberWithoutGHCount = (float) PromotionNumberWithoutGH;
				float GhHireNumberCount = (float) GhHireNumber;
				float NewHireNumberCount = (float) NewHireNumber;

				// 获取百分比实例
				NumberFormat nt = NumberFormat.getPercentInstance();
				// 设置百分数精度为2（保留两位小数）
				nt.setMinimumFractionDigits(2);

				// 所在年份12月的总人数不为0
				if (belongingYearLastMonthCount != 0) {

					// Promotion ratio(All)计算方法
					ratioDto.setPromotAllRatio(nt.format(PromotionNumberCount / lastMonthCount));

					// Promotion ratio(WithoutGH)计算方法
					ratioDto.setPromotWithoutGhRatio(nt.format(PromotionNumberWithoutGHCount / lastMonthCount));

					// GH Hire ratio计算方法
					ratioDto.setGhHireRatio(nt.format(GhHireNumberCount / lastMonthCount));

					// New Hire ratio计算方法
					ratioDto.setNewHireRatio(nt.format(NewHireNumberCount / lastMonthCount));

				} else {

					// 计算结果为“0%”
					ratioDto.setPromotAllRatio("0%");
					ratioDto.setPromotWithoutGhRatio("0%");
					ratioDto.setGhHireRatio("0%");
					ratioDto.setNewHireRatio("0%");

				}

				// 所在年份1月的总人数不为0
				if (belongingYearFirstMonthCount != 0) {

					// Resign ratio计算方法
					ratioDto.setResignRatio(
							nt.format((firstMonthCount - lastMonthCount + NewHireNumberCount) / firstMonthCount));

				} else {

					// 计算结果为“0%”
					ratioDto.setResignRatio("0%");

				}

				// 计算结果的list
				List<RatioDto> calculationResult = new ArrayList<RatioDto>();
				calculationResult.add(ratioDto);

				// 将计算结果list放入ratioDtoList中
				RatioListDto ratioDtoList = new RatioListDto();
				ratioDtoList.setRatioDto(calculationResult);

				// 返回计算结果List
				return Result.success(ratioDtoList);

			}

				// 所在年份不存在，返回error message
				return Result.error("检索结果零件，请重新设定检索条件。");

		}

		// 所在年份 > 当前年份的前一年，返回error message
		return Result.error("最大可查询年份为当前年的前一年。");
	}

	private void setMap(Map<String, Double> sumBandmixMap, Map<String, Long> countMap, double rate,
			BandmixMonthlyDto bandmixMonthlyDto) {
		sumBandmixMap.put("1M", sumBandmixMap.get("1M") + Double.valueOf(bandmixMonthlyDto.getDataJan()) * rate);
		sumBandmixMap.put("2M", sumBandmixMap.get("2M") + Double.valueOf(bandmixMonthlyDto.getDataFeb()) * rate);
		sumBandmixMap.put("3M", sumBandmixMap.get("3M") + Double.valueOf(bandmixMonthlyDto.getDataMar()) * rate);
		sumBandmixMap.put("4M", sumBandmixMap.get("4M") + Double.valueOf(bandmixMonthlyDto.getDataApr()) * rate);
		sumBandmixMap.put("5M", sumBandmixMap.get("5M") + Double.valueOf(bandmixMonthlyDto.getDataMay()) * rate);
		sumBandmixMap.put("6M", sumBandmixMap.get("6M") + Double.valueOf(bandmixMonthlyDto.getDataJun()) * rate);
		sumBandmixMap.put("7M", sumBandmixMap.get("7M") + Double.valueOf(bandmixMonthlyDto.getDataJul()) * rate);
		sumBandmixMap.put("8M", sumBandmixMap.get("8M") + Double.valueOf(bandmixMonthlyDto.getDataAug()) * rate);
		sumBandmixMap.put("9M", sumBandmixMap.get("9M") + Double.valueOf(bandmixMonthlyDto.getDataSep()) * rate);
		sumBandmixMap.put("10M", sumBandmixMap.get("10M") + Double.valueOf(bandmixMonthlyDto.getDataOct()) * rate);
		sumBandmixMap.put("11M", sumBandmixMap.get("11M") + Double.valueOf(bandmixMonthlyDto.getDataNov()) * rate);
		sumBandmixMap.put("12M", sumBandmixMap.get("12M") + Double.valueOf(bandmixMonthlyDto.getDataDec()) * rate);

		countMap.put("1M", countMap.get("1M") + Integer.valueOf(bandmixMonthlyDto.getDataJan()));
		countMap.put("2M", countMap.get("2M") + Integer.valueOf(bandmixMonthlyDto.getDataFeb()));
		countMap.put("3M", countMap.get("3M") + Integer.valueOf(bandmixMonthlyDto.getDataMar()));
		countMap.put("4M", countMap.get("4M") + Integer.valueOf(bandmixMonthlyDto.getDataApr()));
		countMap.put("5M", countMap.get("5M") + Integer.valueOf(bandmixMonthlyDto.getDataMay()));
		countMap.put("6M", countMap.get("6M") + Integer.valueOf(bandmixMonthlyDto.getDataJun()));
		countMap.put("7M", countMap.get("7M") + Integer.valueOf(bandmixMonthlyDto.getDataJul()));
		countMap.put("8M", countMap.get("8M") + Integer.valueOf(bandmixMonthlyDto.getDataAug()));
		countMap.put("9M", countMap.get("9M") + Integer.valueOf(bandmixMonthlyDto.getDataSep()));
		countMap.put("10M", countMap.get("10M") + Integer.valueOf(bandmixMonthlyDto.getDataOct()));
		countMap.put("11M", countMap.get("11M") + Integer.valueOf(bandmixMonthlyDto.getDataNov()));
		countMap.put("12M", countMap.get("12M") + Integer.valueOf(bandmixMonthlyDto.getDataDec()));
	}

	private List<BandmixDto> setBandmixDtoList(String organizationCode, String currentPeM, String currentyear,
			List<BandmixInfo> employeeList) {
		List<BandmixDto> bandmixDtoList = new ArrayList();

		for (int i = 0; i < employeeList.size(); i++) {

			BandmixInfo bandmixInfo = employeeList.get(i);
			BandmixDto bandmixDto = new BandmixDto();
			bandmixDto.setEmployeeSn(bandmixInfo.getId().getEmployeeSn());
			bandmixDto.setPeopleManagerNotesId(bandmixInfo.getId().getPeopleManagerNotesId());

			BeanUtils.copyProperties(bandmixInfo, bandmixDto);

			bandmixDto.setKeyNO(String.valueOf(i + 1));

			bandmixDto.setSearchBelongYear(currentyear);
			bandmixDto.setSearchBelongTowerCode(organizationCode);
			bandmixDto.setSearchPemNotesId(currentPeM);

			bandmixDtoList.add(bandmixDto);
		}
		return bandmixDtoList;
	}

	private String scaleData(String strData, Long longData) {
		if(longData!=0) {
			return new BigDecimal(strData).multiply(BigDecimal.valueOf(100)).divide(new BigDecimal(longData), 2, RoundingMode.HALF_EVEN).toString()+ "%";
		} else {
			return "0";
		}
	}
	
	private String scaleData(Double doubleData, Long longData) {
		if(longData!=0) {
			return new BigDecimal(doubleData).divide(new BigDecimal(longData), 2, RoundingMode.HALF_EVEN).toString() ;
		} else {
			return "0";
		}
	}

	/**
	 * 
	 * 判断对应月份及对应band等级，将其人数+1
	 * 
	 * @param promotionDto
	 * @param month        当前月份
	 * @param bandLevel    band等级
	 * @return promotionDto
	 * 
	 */
	private PromotionDto PromotionMonthAndLevel(PromotionDto promotionDto, int month, String bandLevel) {

		// 月份为1月
		if (month == 1) {

			if (("6A").equals(bandLevel)) {

				// 1月升6A数+1
				promotionDto.setB6AJan(promotionDto.getB6AJan() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 1月升6B数+1
				promotionDto.setB6BJan(promotionDto.getB6BJan() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 1月升7A数+1
				promotionDto.setB7AJan(promotionDto.getB7AJan() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 1月升7B数+1
				promotionDto.setB7BJan(promotionDto.getB7BJan() + 1);

			} else if (("8").equals(bandLevel)) {

				// 1月升8数+1
				promotionDto.setB8Jan(promotionDto.getB8Jan() + 1);

			} else {

				// 1月升9数+1
				promotionDto.setB9Jan(promotionDto.getB9Jan() + 1);

			}

		}

		// 月份为2月
		else if (month == 2) {

			if (("6A").equals(bandLevel)) {

				// 2月升6A数+1
				promotionDto.setB6AFeb(promotionDto.getB6AFeb() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 2月升6B数+1
				promotionDto.setB6BFeb(promotionDto.getB6BFeb() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 2月升7A数+1
				promotionDto.setB7AFeb(promotionDto.getB7AFeb() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 2月升7B数+1
				promotionDto.setB7BFeb(promotionDto.getB7BFeb() + 1);

			} else if (("8").equals(bandLevel)) {

				// 2月升8数+1
				promotionDto.setB8Feb(promotionDto.getB8Feb() + 1);

			} else {

				// 2月升9数+1
				promotionDto.setB9Feb(promotionDto.getB9Feb() + 1);

			}

		}

		// 月份为3月
		else if (month == 3) {

			if (("6A").equals(bandLevel)) {

				// 3月升6A数+1
				promotionDto.setB6AMar(promotionDto.getB6AMar() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 3月升6B数+1
				promotionDto.setB6BMar(promotionDto.getB6BMar() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 3月升7A数+1
				promotionDto.setB7AMar(promotionDto.getB7AMar() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 3月升7B数+1
				promotionDto.setB7BMar(promotionDto.getB7BMar() + 1);

			} else if (("8").equals(bandLevel)) {

				// 3月升8数+1
				promotionDto.setB8Mar(promotionDto.getB8Mar() + 1);

			} else {

				// 3月升9数+1
				promotionDto.setB9Mar(promotionDto.getB9Mar() + 1);

			}

		}

		// 月份为4月
		else if (month == 4) {

			if (("6A").equals(bandLevel)) {

				// 4月升6A数+1
				promotionDto.setB6AApr(promotionDto.getB6AApr() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 4月升6B数+1
				promotionDto.setB6BApr(promotionDto.getB6BApr() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 4月升7A数+1
				promotionDto.setB7AApr(promotionDto.getB7AApr() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 4月升7B数+1
				promotionDto.setB7BApr(promotionDto.getB7BApr() + 1);

			} else if (("8").equals(bandLevel)) {

				// 4月升8数+1
				promotionDto.setB8Apr(promotionDto.getB8Apr() + 1);

			} else {

				// 4月升9数+1
				promotionDto.setB9Apr(promotionDto.getB9Apr() + 1);

			}

		}

		// 月份为5月
		else if (month == 5) {

			if (("6A").equals(bandLevel)) {

				// 5月升6A数+1
				promotionDto.setB6AMay(promotionDto.getB6AMay() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 5月升6B数+1
				promotionDto.setB6BMay(promotionDto.getB6BMay() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 5月升7A数+1
				promotionDto.setB7AMay(promotionDto.getB7AMay() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 5月升7B数+1
				promotionDto.setB7BMay(promotionDto.getB7BMay() + 1);

			} else if (("8").equals(bandLevel)) {

				// 5月升8数+1
				promotionDto.setB8May(promotionDto.getB8May() + 1);

			} else {

				// 5月升9数+1
				promotionDto.setB9May(promotionDto.getB9May() + 1);

			}

		}

		// 月份为6月
		else if (month == 6) {

			if (("6A").equals(bandLevel)) {

				// 6月升6A数+1
				promotionDto.setB6AJun(promotionDto.getB6AJun() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 6月升6B数+1
				promotionDto.setB6BJun(promotionDto.getB6BJun() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 6月升7A数+1
				promotionDto.setB7AJun(promotionDto.getB7AJun() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 6月升7B数+1
				promotionDto.setB7BJun(promotionDto.getB7BJun() + 1);

			} else if (("8").equals(bandLevel)) {

				// 6月升8数+1
				promotionDto.setB8Jun(promotionDto.getB8Jun() + 1);

			} else {

				// 6月升9数+1
				promotionDto.setB9Jun(promotionDto.getB9Jun() + 1);

			}

		}

		// 月份为7月
		else if (month == 7) {

			if (("6A").equals(bandLevel)) {

				// 7月升6A数+1
				promotionDto.setB6AJul(promotionDto.getB6AJul() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 7月升6B数+1
				promotionDto.setB6BJul(promotionDto.getB6BJul() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 7月升7A数+1
				promotionDto.setB7AJul(promotionDto.getB7AJul() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 7月升7B数+1
				promotionDto.setB7BJul(promotionDto.getB7BJul() + 1);

			} else if (("8").equals(bandLevel)) {

				// 7月升8数+1
				promotionDto.setB8Jul(promotionDto.getB8Jul() + 1);

			} else {

				// 7月升9数+1
				promotionDto.setB9Jul(promotionDto.getB9Jul() + 1);

			}

		}

		// 月份为8月
		else if (month == 8) {

			if (("6A").equals(bandLevel)) {

				// 8月升6A数+1
				promotionDto.setB6AAug(promotionDto.getB6AAug() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 8月升6B数+1
				promotionDto.setB6BAug(promotionDto.getB6BAug() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 8月升7A数+1
				promotionDto.setB7AAug(promotionDto.getB7AAug() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 8月升7B数+1
				promotionDto.setB7BAug(promotionDto.getB7BAug() + 1);

			} else if (("8").equals(bandLevel)) {

				// 8月升8数+1
				promotionDto.setB8Aug(promotionDto.getB8Aug() + 1);

			} else {

				// 8月升9数+1
				promotionDto.setB9Aug(promotionDto.getB9Aug() + 1);

			}

		}

		// 月份为9月
		else if (month == 9) {

			if (("6A").equals(bandLevel)) {

				// 9月升6A数+1
				promotionDto.setB6ASep(promotionDto.getB6ASep() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 9月升6B数+1
				promotionDto.setB6BSep(promotionDto.getB6BSep() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 9月升7A数+1
				promotionDto.setB7ASep(promotionDto.getB7ASep() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 9月升7B数+1
				promotionDto.setB7BSep(promotionDto.getB7BSep() + 1);

			} else if (("8").equals(bandLevel)) {

				// 9月升8数+1
				promotionDto.setB8Sep(promotionDto.getB8Sep() + 1);

			} else {

				// 9月升9数+1
				promotionDto.setB9Sep(promotionDto.getB9Sep() + 1);

			}

		}

		// 月份为10月
		else if (month == 10) {

			if (("6A").equals(bandLevel)) {

				// 10月升6A数+1
				promotionDto.setB6AOct(promotionDto.getB6AOct() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 10月升6B数+1
				promotionDto.setB6BOct(promotionDto.getB6BOct() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 10月升7A数+1
				promotionDto.setB7AOct(promotionDto.getB7AOct() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 10月升7B数+1
				promotionDto.setB7BOct(promotionDto.getB7BOct() + 1);

			} else if (("8").equals(bandLevel)) {

				// 10月升8数+1
				promotionDto.setB8Oct(promotionDto.getB8Oct() + 1);

			} else {

				// 10月升9数+1
				promotionDto.setB9Oct(promotionDto.getB9Oct() + 1);

			}

		}

		// 月份为11月
		else if (month == 11) {

			if (("6A").equals(bandLevel)) {

				// 11月升6A数+1
				promotionDto.setB6ANov(promotionDto.getB6ANov() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 11月升6B数+1
				promotionDto.setB6BNov(promotionDto.getB6BNov() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 11月升7A数+1
				promotionDto.setB7ANov(promotionDto.getB7ANov() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 11月升7B数+1
				promotionDto.setB7BNov(promotionDto.getB7BNov() + 1);

			} else if (("8").equals(bandLevel)) {

				// 11月升8数+1
				promotionDto.setB8Nov(promotionDto.getB8Nov() + 1);

			} else {

				// 11月升9数+1
				promotionDto.setB9Nov(promotionDto.getB9Nov() + 1);

			}

		}

		// 月份为12月
		else {

			if (("6A").equals(bandLevel)) {

				// 12月升6A数+1
				promotionDto.setB6ADec(promotionDto.getB6ADec() + 1);

			} else if (("6B").equals(bandLevel)) {

				// 12月升6B数+1
				promotionDto.setB6BDec(promotionDto.getB6BDec() + 1);

			} else if (("7A").equals(bandLevel)) {

				// 12月升7A数+1
				promotionDto.setB7ADec(promotionDto.getB7ADec() + 1);

			} else if (("7B").equals(bandLevel)) {

				// 12月升7B数+1
				promotionDto.setB7BDec(promotionDto.getB7BDec() + 1);

			} else if (("8").equals(bandLevel)) {

				// 12月升8数+1
				promotionDto.setB8Dec(promotionDto.getB8Dec() + 1);

			} else {

				// 12月升9数+1
				promotionDto.setB9Dec(promotionDto.getB9Dec() + 1);

			}

		}

		// 返回结果
		return promotionDto;
	}

}

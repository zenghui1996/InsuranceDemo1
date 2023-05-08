package com.bandmix.userapi.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.bandmix.commonapi.constants.UserConstant;
import com.bandmix.commonapi.model.dto.LoginDto;
import com.bandmix.commonapi.model.dto.UserDto;
import com.bandmix.commonapi.model.reponse.Result;
import com.bandmix.userapi.entity.OrganizationInfo;
import com.bandmix.userapi.entity.UserInfo;
import com.bandmix.userapi.entity.UserToken;
import com.bandmix.userapi.repository.OrganizationInfoRepository;
import com.bandmix.userapi.repository.UserInfoRepository;
import com.bandmix.userapi.repository.UserTokenRepository;
import com.bandmix.userapi.utils.TransformUtil;

@Service
public class UserInfoService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private UserTokenRepository userTokenRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${security.oauth2.client.access-token-uri}")
	private String oauthUrl;

	@Autowired
	private JavaMailSender sender;

	@Value("${app.mail.subject}")
	private String mailSubject;

	@Value("${app.mail.body}")
	private String mailBody;

	@Value("${spring.mail.username}")
	private String mailFrom;

	private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public Result createOrUpdateUser(UserDto user) {

		// find user by userName
		List<UserInfo> userList = userInfoRepository.findByUserName(user.getUserName());
		if (userList != null && userList.size() > 0) {
			return Result.error("用户[" + user.getUserName() + "]已经存在!");
		}

		String password = "{bcrypt}" + passwordEncoder.encode(user.getPasswordEncrypt());
		user.setPasswordEncrypt(password);
		UserInfo userInfoEntity = new UserInfo();
		BeanUtils.copyProperties(user, userInfoEntity);
		userInfoRepository.save(userInfoEntity);

		return Result.success(userInfoEntity);
	}

	public Result login(UserDto userDto) {
		List<String> checkList = new ArrayList<>();
		if (StringUtils.isEmpty(userDto.getUserId())) {
			checkList.add("Please Enter UserId!");
		}

		if (StringUtils.isEmpty(userDto.getPasswordEncrypt())) {
			checkList.add("Please Enter Password!");
		}

		if (checkList != null && checkList.size() > 0) {
			return Result.checkError(checkList);
		}

		UserInfo userInfo = userInfoRepository.findByUserIdAndLogicDeleteFlag(userDto.getUserId(),
				UserConstant.IS_NOT_DEL);

		if (userInfo != null && !StringUtils.isEmpty(userInfo.getUserId())) {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "password");
			map.add("client_id", "client");
			map.add("client_secret", "123456");
			map.add("username", userDto.getUserId());
			map.add("password", userDto.getPasswordEncrypt());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			Map<String, String> tokenMap = restTemplate.postForObject(oauthUrl, request, Map.class);
			if (tokenMap.isEmpty()) {
				return Result.error("Message0007");
			} else {
				if (tokenMap.containsKey("accessToken")) {

					UserToken userToken = userTokenRepository.findByTokenId("Bearer" + tokenMap.get("accessToken"));
					if (userToken != null) {
						userTokenRepository.deleteById("Bearer" + tokenMap.get("accessToken"));
					}

					userToken = new UserToken();
					userToken.setTokenId("Bearer" + tokenMap.get("accessToken"));
					userToken.setUserId(userInfo.getUserId());
					userToken.setAdminnistratorFlag(userInfo.getAdminnistratorFlag());
					userToken.setOrganizationAuthorityCode(userInfo.getOrganizationAuthorityCode());
					userToken.setVersion("1");
					userToken.setUpdateUserId("1");
					userToken.setUpdateProgramId("1");
					userToken.setRegisterUserId("1");
					userToken.setRegisterProgramId("1");

					userTokenRepository.save(userToken);

					LoginDto loginDto = new LoginDto();
					loginDto.setUserId(userInfo.getUserId());
					loginDto.setUserName(userInfo.getUserName());
					loginDto.setAdminnistratorFlag(userInfo.getAdminnistratorFlag());
					loginDto.setOrganizationAuthorityCode(userInfo.getOrganizationAuthorityCode());
					loginDto.setAccessToken(tokenMap.get("accessToken"));
					loginDto.setRefreshToken(tokenMap.get("refreshToken"));

					return Result.success(loginDto);
				} else if (tokenMap.containsKey("error_description")) {
					return Result.error(tokenMap.get("error_description"));
				}
			}
		}
		return Result.error("Message0006");
	}

	public Result queryUserDetail(String userId) {
		Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
		if (userInfo.isPresent()) {
			UserDto userDto = TransformUtil.generateUserDto(userInfo.get());

			return Result.success(userDto);
		}
		return Result.error("用户不存在");
	}

//    public Result queryUserList(Integer isActive, String userName, String roleCode) {
//
//        List<UserInfo> userInfoList = new ArrayList<>();
//        if(isActive != null && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(roleCode)){
//            userInfoList = userInfoRepository.findByUserNameAndRoleCodeAndIsActiveOrderByLastUpdTimeDesc(userName,roleCode,isActive);
//        }else if(isActive != null && StringUtils.isEmpty(userName) && !StringUtils.isEmpty(roleCode)){
//            userInfoList = userInfoRepository.findByRoleCodeAndIsActiveOrderByLastUpdTimeDesc(roleCode,isActive);
//        }else if(isActive == null && StringUtils.isEmpty(userName) && !StringUtils.isEmpty(roleCode)){
//            userInfoList = userInfoRepository.findByRoleCodeOrderByLastUpdTimeDesc(roleCode);
//        }else if(isActive == null && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(roleCode)){
//            userInfoList = userInfoRepository.findByUserNameAndRoleCodeOrderByLastUpdTimeDesc(userName,roleCode);
//        }
//
//        if(userInfoList != null && userInfoList.size() > 0){
//            return Result.success(userInfoList);
//        }
//
//        return Result.error("No " + roleCode + " Data!");
//    }

	public Result updateUser(UserDto userDto) {

		List<String> checkList = new ArrayList<>();
		if (StringUtils.isEmpty(userDto.getUserId())) {
			checkList.add("Please Enter UserId!");
		}

		// ...

		if (checkList != null && checkList.size() > 0) {
			return Result.checkError(checkList);
		}

		Optional<UserInfo> userInfo = userInfoRepository.findById(userDto.getUserId());
		if (userInfo.isPresent()) {
			UserInfo user = userInfo.get();
			// update ****
			userInfoRepository.save(user);
			return Result.success(user);
		}

		return Result.error("Update User error!");
	}

	public Result deleteUser(String userId) {

		Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
		if (userInfo.isPresent()) {
			UserInfo user = userInfo.get();
			user.setLogicDeleteFlag("1");
			userInfoRepository.save(user);
			return Result.success(userInfo.get());
		}

		return Result.error("Delete User error!");
	}

	// 密码重置
	public Result resetUserPassword(UserDto userDto, String authorization) {

		// 用户ID不能为空
		List<String> checkList = new ArrayList<>();
		if (StringUtils.isEmpty(userDto.getUserId())) {
			checkList.add("Please Enter UserId!");
		}

		// 旧密码不能是空
		String oldPassword = userDto.getOldPassword();
		if (StringUtils.isEmpty(oldPassword)) {
			checkList.add("Please Enter OldPassword!");
		}

		// 新密码不能是空
		String newPassword = userDto.getNewPassword();
		if (StringUtils.isEmpty(newPassword)) {
			checkList.add("Please Enter NewPassword!");
		}

		if (checkList != null && checkList.size() > 0) {
			return Result.checkError(checkList);
		}

		// token取得
		UserToken userToken = userTokenRepository.findByTokenId(authorization);

		// 查询用户信息
		UserInfo userInfo = userInfoRepository.findByUserIdAndLogicDeleteFlag(userDto.getUserId(),
				UserConstant.IS_NOT_DEL);

		// 用户信息空值校验
		if (userInfo != null && !StringUtils.isEmpty(userInfo.getUserId())) {
			// 存储用户信息
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "password");
			map.add("client_id", "client");
			map.add("client_secret", "123456");
			map.add("username", userDto.getUserId());
			map.add("password", oldPassword);

			HttpHeaders headers = new HttpHeaders();
			// 密码加密
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			// 请求资源
			Map<String, String> tokenMap = restTemplate.postForObject(oauthUrl, request, Map.class);

			if (tokenMap.isEmpty()) {
				return Result.error("Message0007");
			} else {
				// 保存修改后的密码
				if (tokenMap.containsKey("accessToken")) {
					String password = "{bcrypt}" + passwordEncoder.encode(newPassword);
					userInfo.setPasswordEncrypt(password);

					userInfoRepository.save(userInfo);

					return Result.success(null);
				} else if (tokenMap.containsKey("error_description")) {
					return Result.error(tokenMap.get("error_description"));
				}
			}
		}
		return Result.error("Message0007");
	}

	private void sendMail(String userName, String password) {

		System.out.println("===========send mail===============");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailFrom);
		message.setTo(userName);
		message.setSubject(mailSubject);
		String text = MessageFormat.format(mailBody, password);
		message.setText(text);
		sender.send(message);
		System.out.println("===========send mail success=======");
	}

	public Result getUserManagementSearch(String organizationCode, String userId) {
		// 用户管理查询API
		List<UserInfo> userInfoList = new ArrayList<>();
		if (!StringUtils.isEmpty(userId)) {
			userInfoList = userInfoRepository.findByUserId(userId);
		} else if (!StringUtils.isEmpty(organizationCode)) {
			userInfoList = userInfoRepository.findByOrganizationAuthorityCodeLike(organizationCode + "%");
		} else {
			userInfoList = userInfoRepository.findAll();
		}

		if (userInfoList.size() > 0) {
			List<UserDto> userDtoList = new ArrayList<>();
			for (UserInfo userInfomanagement : userInfoList) {
				// 管理者Flag
				String adminnistratorFlagStr = userInfomanagement.getAdminnistratorFlag();
				if ("1".equals(adminnistratorFlagStr)) {
					adminnistratorFlagStr = "普通用户";
				} else if ("2".equals(adminnistratorFlagStr)) {
					adminnistratorFlagStr = "Acount管理者";
				} else if ("3".equals(adminnistratorFlagStr)) {
					adminnistratorFlagStr = "超级管理者";
				}

				// 组织code名
				String organizationAuthorityCode = userInfomanagement.getOrganizationAuthorityCode();
				String organizationAuthorityName = userInfomanagement.getOrganizationAuthorityCode();
				// 组织权限code的长度是3的场合
				if (organizationAuthorityCode.length() == 3) {
					// 组织权限code的前三位
					String userAdminnistratorFlagTopThree = organizationAuthorityCode.substring(0);

					List<OrganizationInfo> organizationCodeList = organizationInfoRepository
							.findByIdOrganizationCode(userAdminnistratorFlagTopThree);

					if (organizationCodeList.size() > 0) {
						organizationAuthorityName = organizationCodeList.get(0).getOrganizationCodeDescription();
					}
				}

				// 组织权限code的长度是6的场合
				if (organizationAuthorityCode.length() == 6) {
					// 组织权限code的前三位、组织权限code的后三位
					String userAdminnistratorFlagTopThree = organizationAuthorityCode.substring(0, 3);
					String userAdminnistratorFlagEndThree = organizationAuthorityCode.substring(3, 6);

					List<OrganizationInfo> organizationCodeList = organizationInfoRepository
							.findByIdOrganizationCodeAndIdSectorCode(userAdminnistratorFlagTopThree,
									userAdminnistratorFlagEndThree);
					if (organizationCodeList.size() > 0) {
						organizationAuthorityName = organizationCodeList.get(0).getOrganizationCodeDescription() + "/"
								+ organizationCodeList.get(0).getSectorCodeDescription();
					}
				}

				// 组织权限code的长度是9的场合
				if (organizationAuthorityCode.length() == 9) {
					// 组织权限code的前三位、组织权限code的中间三位、组织权限code的后三位
					String userAdminnistratorFlagTopThree = organizationAuthorityCode.substring(0, 3);
					String userAdminnistratorFlagMiddleThree = organizationAuthorityCode.substring(3, 6);
					String userAdminnistratorFlagEndThree = organizationAuthorityCode.substring(6, 9);

					List<OrganizationInfo> organizationCodeList = organizationInfoRepository
							.findByIdOrganizationCodeAndIdSectorCodeAndIdTowerCode(userAdminnistratorFlagTopThree,
									userAdminnistratorFlagMiddleThree, userAdminnistratorFlagEndThree);
					if (organizationCodeList.size() > 0) {
						organizationAuthorityName = organizationCodeList.get(0).getOrganizationCodeDescription() + "/"
								+ organizationCodeList.get(0).getSectorCodeDescription() + "/"
								+ organizationCodeList.get(0).getTowerCodeDescription();
					}
				}

				UserDto userDto = new UserDto();

				userDto.setUserId(userInfomanagement.getUserId());
				userDto.setUserName(userInfomanagement.getUserName());
				userDto.setAdminnistratorFlag(userInfomanagement.getAdminnistratorFlag());
				userDto.setAdminnistratorFlagStr(adminnistratorFlagStr);
				userDto.setOrganizationAuthorityCode(organizationAuthorityCode);
				userDto.setOrganizationAuthorityName(organizationAuthorityName);
				userDto.setEmployeeNotesId(userInfomanagement.getEmployeeNotesId());

				userDtoList.add(userDto);
			}
			return Result.success(userDtoList);
		} else {
			return Result.error("用户管理信息不存在!");
		}
	}

	public Result userManagementSave(UserDto dto) {

		// 更新
		List<UserInfo> userInfo = userInfoRepository.findByUserId(dto.getUserId());
		if (userInfo.size() > 0) {
			UserInfo user = userInfo.get(0);

			user.setUserName(dto.getUserName());
			user.setAdminnistratorFlag(dto.getAdminnistratorFlag());
			user.setOrganizationAuthorityCode(dto.getOrganizationAuthorityCode());
			user.setEmployeeNotesId(dto.getEmployeeNotesId());

			userInfoRepository.save(user);
			return getUserManagementSearch("", dto.getUserId());
		}

		// 新建
		String password = "{bcrypt}" + passwordEncoder.encode(dto.getUserId());

		UserInfo userInfoEntity = new UserInfo();

		userInfoEntity.setUserId(dto.getUserId());
		userInfoEntity.setUserName(dto.getUserName());
		userInfoEntity.setPasswordEncrypt(password);
		userInfoEntity.setAdminnistratorFlag(dto.getAdminnistratorFlag());
		userInfoEntity.setOrganizationAuthorityCode(dto.getOrganizationAuthorityCode());
		userInfoEntity.setEmployeeNotesId(dto.getEmployeeNotesId());
		userInfoEntity.setEmployeeNotesMail("");
		userInfoEntity.setLogicDeleteFlag("0");
		userInfoEntity.setVersion("1");
		userInfoEntity.setUpdateUserId("1");
		userInfoEntity.setUpdateProgramId("1");
		userInfoEntity.setRegisterUserId("1");
		userInfoEntity.setRegisterProgramId("1");

		userInfoRepository.save(userInfoEntity);
		return getUserManagementSearch("", dto.getUserId());
	}

	public Result userManagementDelete(String userId) {
		List<UserInfo> userInfo = userInfoRepository.findByUserId(userId);
		if (userInfo.size() > 0) {
			userInfoRepository.deleteById(userId);
			return Result.success("用户管理信息删除成功!");
		}

		return Result.error("用户管理信息删除失败!");
	}
}

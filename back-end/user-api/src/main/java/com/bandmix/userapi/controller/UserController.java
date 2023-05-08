package com.bandmix.userapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bandmix.commonapi.model.dto.UserDto;
import com.bandmix.commonapi.model.reponse.Result;
import com.bandmix.userapi.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	private UserInfoService userInfoService;

	@PostMapping("/registry")
	public ResponseEntity<Result> registry(@RequestBody UserDto user) {

		Result result = userInfoService.createOrUpdateUser(user);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<Result> login(@RequestBody UserDto user) {
		Result result = userInfoService.login(user);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@GetMapping("/user/detail/{userId}")
	public ResponseEntity<Result> queryUserDetail(@PathVariable(name = "userId") String userId) {
		Result result = userInfoService.queryUserDetail(userId);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

//    @GetMapping("/user/search")
//    public ResponseEntity<Result> queryUserList(@RequestParam(name = "isActive", required = false) Integer isActive,
//                                                   @RequestParam(name = "userName", required = false) String userName,
//                                                   @RequestParam(name = "roleCode") String roleCode){
//
//        Result result = userInfoService.queryUserList(isActive, userName, roleCode);
//        return new ResponseEntity<Result>(result, HttpStatus.OK);
//    }

	@PutMapping("/user/update")
	public ResponseEntity<Result> updateUser(@RequestBody UserDto user) {

		Result result = userInfoService.updateUser(user);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@DeleteMapping("/user/delete")
//    @PreAuthorize("hasAnyAuthority('1')")
	public ResponseEntity<Result> deleteUser(@RequestParam(name = "userId") String userId) {

		Result result = userInfoService.deleteUser(userId);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@PostMapping("/user/resetPwd")
	@PreAuthorize("hasAnyAuthority('1')")
	public ResponseEntity<Result> resetUserPassword(@RequestBody UserDto user,
			@RequestHeader("Authorization") String authorization) {

		Result result = userInfoService.resetUserPassword(user, authorization);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@GetMapping("/userManagementSearch")
	public ResponseEntity<Result> getUserManagementSearch(
			@RequestParam(name = "organizationCode") String organizationCode,
			@RequestParam(name = "userId") String userId) {
		Result result = userInfoService.getUserManagementSearch(organizationCode, userId);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@PostMapping("/userManagementSave")
	public ResponseEntity<Result> userManagementSave(@RequestBody UserDto dto) {
		Result result = userInfoService.userManagementSave(dto);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	@GetMapping("/userManagementDelete")
	public ResponseEntity<Result> userManagementDelete(@RequestParam(name = "userId") String userId) {
		Result result = userInfoService.userManagementDelete(userId);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}
}

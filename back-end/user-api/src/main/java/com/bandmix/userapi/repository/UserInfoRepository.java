package com.bandmix.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandmix.userapi.entity.UserInfo;

import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

	UserInfo findByUserIdAndLogicDeleteFlag(String userId, String logicDeleteFlag);

	List<UserInfo> findByUserName(String userName);

	List<UserInfo> findByUserId(String userId);

	List<UserInfo> findByOrganizationAuthorityCodeLike(String organizationAuthorityCode);

//    List<UserInfo> findByUserNameAndRoleCodeAndIsActiveOrderByLastUpdTimeDesc(String userName, String roleCode, Integer isActive);
//
//    List<UserInfo> findByRoleCodeAndIsActiveOrderByLastUpdTimeDesc(String roleCode, Integer isActive);
//
//    List<UserInfo> findByRoleCodeOrderByLastUpdTimeDesc(String roleCode);
//
//    List<UserInfo> findByUserNameAndRoleCodeOrderByLastUpdTimeDesc(String userName, String roleCode);
//
//    List<UserInfo> findByUserNameNot(String userName);
}

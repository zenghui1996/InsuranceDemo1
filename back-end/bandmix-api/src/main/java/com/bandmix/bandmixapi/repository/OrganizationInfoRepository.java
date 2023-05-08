package com.bandmix.bandmixapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandmix.bandmixapi.entity.OrganizationInfo;
import com.bandmix.bandmixapi.entity.OrganizationfoKey;

@Repository
public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, OrganizationfoKey> {

	// 组织Code取得（全部）
	List<OrganizationInfo> findAll();
//		List<OrganizationCode> findAll();

	// 组织Code取得（组织别）
	List<OrganizationInfo> findByIdOrganizationCode(String organizationCode);

	// 组织Code取得（部门别）
	List<OrganizationInfo> findByIdOrganizationCodeAndIdSectorCode(String organizationCode, String sectorCode);

	// 组织Code取得（Tower别）
	List<OrganizationInfo> findByIdOrganizationCodeAndIdSectorCodeAndIdTowerCode(String organizationCode, String towerCode,
			String sectorCode);

}

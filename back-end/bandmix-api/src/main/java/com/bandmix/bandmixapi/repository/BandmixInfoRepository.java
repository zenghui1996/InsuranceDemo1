package com.bandmix.bandmixapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bandmix.bandmixapi.entity.BandmixInfo;
import com.bandmix.bandmixapi.entity.BandmixInfoKey;
import com.bandmix.commonapi.model.dto.BandMixQuarterDto;
import com.bandmix.commonapi.model.dto.BandmixMonthlyDto;

@Repository
public interface BandmixInfoRepository extends JpaRepository<BandmixInfo, BandmixInfoKey> {

	@Modifying
	Long deleteByIdBelongingTowerCodeAndIdBelongingYear(String belongingTowerCode, String belongingYear);

	// 员工查询
	@Query(value = "SELECT *" + "	FROM employee_leve_tbl as a " + "	WHERE  a.belonging_year = :belongingYear"
			+ "	 AND a.belonging_tower_code like CONCAT(:belongingTowerCode,'%') "
			+ "	AND a.people_manager_notes_id =:pem", nativeQuery = true)
	List<BandmixInfo> findByBelongingTowerCodeAndPeopleManagerNotesidAndBelongingYear(
			@Param("belongingTowerCode") String belongingTowerCode, @Param("pem") String pem,
			@Param("belongingYear") String belongingYear);

	// 员工查询
	@Query(value = "SELECT *" + "	FROM employee_leve_tbl as a " + "	WHERE  a.belonging_year = :belongingYear"
			+ "	 AND a.belonging_tower_code like CONCAT(:belongingTowerCode,'%') ", nativeQuery = true)
	List<BandmixInfo> findByBelongingTowerCodeAndPemNullAndBelongingYear(
			@Param("belongingTowerCode") String belongingTowerCode, @Param("belongingYear") String belongingYear);

	@Query(name = "BandmixInfo.getBandMixByQuarter", nativeQuery = true)
	List<BandMixQuarterDto> getBandMixByQuarter(@Param("belongingYear") String belongingYear,
			@Param("towerCode") String towerCode);

	@Query(name = "BandmixInfo.getBandMixByMonthly", nativeQuery = true)
	List<BandmixMonthlyDto> findByBelongingYearAndBelongingTowerCodeLike(@Param("towerCode") String towerCode,
			@Param("belongingYear") String belongingYear);

	// 新入职
	@Query(value = "SELECT b.december_level as beforyersdecember,a.january_level" + ",a.february_level"
			+ ",a.march_level" + ",a.april_level" + ",a.may_level" + ",a.june_level" + ",a.july_level"
			+ ",a.august_level" + ",a.september_level" + ",a.october_level" + ",a.november_level" + ",a.december_level "
			+ "FROM employee_leve_tbl AS a "
			+ "LEFT JOIN (SELECT * FROM employee_leve_tbl WHERE belonging_year = :belongingYear -1 AND belonging_tower_code like CONCAT(:towerCode,'%')) AS b "
			+ "ON a.employee_sn = b.employee_sn " + "WHERE a.belonging_year = :belongingYear "
			+ "AND a.belonging_tower_code like CONCAT(:towerCode,'%')", nativeQuery = true)

	List<Object[]> findNewHireByBelongingYearAndBelongingTowerCodeLike(@Param("belongingYear") String belongingYear,
			@Param("towerCode") String towerCode);

	// 离职
	@Query(value = "SELECT a.employee_sn," + "b.december_level as beforyersdecember,a.january_level"
			+ ",a.february_level" + ",a.march_level" + ",a.april_level" + ",a.may_level" + ",a.june_level"
			+ ",a.july_level" + ",a.august_level" + ",a.september_level" + ",a.october_level" + ",a.november_level"
			+ ",a.december_level " + "FROM employee_leve_tbl AS a "
			+ "LEFT JOIN (SELECT * FROM employee_leve_tbl WHERE belonging_year = :belongingYear -1 AND belonging_tower_code like CONCAT(:towerCode,'%')) AS b "
			+ "ON a.employee_sn = b.employee_sn " + "WHERE a.belonging_year = :belongingYear "
			+ "AND a.belonging_tower_code like CONCAT(:towerCode,'%')", nativeQuery = true)

	List<Object[]> findResignByBelongingYearAndBelongingTowerCodeLike(@Param("belongingYear") String belongingYear,
			@Param("towerCode") String towerCode);

	//查询所在年份去年12月~当年12月band
	@Query(value = "SELECT b.december_level as beforyersdecember,a.january_level"
			+ ",a.february_level"
			+ ",a.march_level"
			+ ",a.april_level"
			+ ",a.may_level"
			+ ",a.june_level"
			+ ",a.july_level"
			+ ",a.august_level"
			+ ",a.september_level"
			+ ",a.october_level"
			+ ",a.november_level"
			+ ",a.december_level "
			+ "FROM employee_leve_tbl AS a "
			+ "LEFT JOIN (SELECT * FROM employee_leve_tbl WHERE belonging_year = :belongingYear -1 AND belonging_tower_code like CONCAT(:towerCode,'%')) AS b "
			+ "ON a.employee_sn = b.employee_sn "
			+ "AND a.people_manager_notes_id = b.people_manager_notes_id "
			+ "AND a.belonging_year -1 =b.belonging_year "
			+ "WHERE a.belonging_year = :belongingYear "
			+ "AND a.belonging_tower_code like CONCAT(:towerCode,'%')", nativeQuery = true)
		
	List<Object[]> findByBelongingTowerCodeAndBelongingYear(@Param("towerCode") String towerCode,@Param("belongingYear") String belongingYear);

	//查询1月总人数
	@Query(value = "SELECT COUNT(january_level) FROM employee_leve_tbl "
				+ "WHERE belonging_year = :belongingYear "
				+ "AND january_level <> '-' "
				+ "AND belonging_tower_code like CONCAT(:towerCode,'%')", nativeQuery = true)
		
	int findByBelongingYearFirstMonthCount(@Param("towerCode") String towerCode,@Param("belongingYear") String belongingYear);

	//查询12月总人数
	@Query(value = "SELECT COUNT(december_level) FROM employee_leve_tbl "
				+ "WHERE belonging_year = :belongingYear "
				+ "AND december_level <> '-' "
				+ "AND belonging_tower_code like CONCAT(:towerCode,'%')", nativeQuery = true)
		
	int findByBelongingYearLastMonthCount(@Param("towerCode") String towerCode,@Param("belongingYear") String belongingYear);
	
}

package com.bandmix.userapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bandmix.userapi.entity.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, String>{

	UserToken findByTokenId(String tokenId);

}

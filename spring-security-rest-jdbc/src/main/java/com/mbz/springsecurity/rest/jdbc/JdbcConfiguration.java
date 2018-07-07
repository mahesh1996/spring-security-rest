package com.mbz.springsecurity.rest.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mbz.springsecurity.rest.token.storage.TokenStorageService;

@Configuration
public class JdbcConfiguration {

	@Bean("tokenStorageService")
	public TokenStorageService tokenStorageService() {
		return new JpaTokenStorageService();
	}
}

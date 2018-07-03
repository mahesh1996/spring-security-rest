package com.mbz.springsecurity.rest.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mbz.springclient.jdbc.AuthenticationRepository;
import com.mbz.springsecurity.rest.token.storage.TokenStorageService;

@Configuration
@ComponentScan(basePackages = "com.mbz.springsecurity.rest")
public class JdbcConfig {

	@Bean("tokenStorageService")
	public TokenStorageService tokenStorageService() {
		return new JpaTokenStorageService();
	}
}

package com.mbz.springsecurity.rest.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.mbz.springsecurity.rest.RestAuthenticationProvider;
import com.mbz.springsecurity.rest.RestTokenValidationFilter;

@Configuration
@Order(1)
public class BeanConfiguration {
	
	@Bean("authenticationDetailsSource")
	public AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource() {
		return new WebAuthenticationDetailsSource();
	}
	
	@Bean("tokenAuthenticationProvider")
	public RestAuthenticationProvider tokenAuthenticationProvider() {
		return new RestAuthenticationProvider();
	}
	
	@Bean
	public RestTokenValidationFilter restTokenValidationFilter() {
		return new RestTokenValidationFilter();
	}
}

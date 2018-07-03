package com.mbz.springsecurity.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.security.rest")
public class SecurityConfigurationProperties {
	
	private String domainClass;
	private String authenticationHeader;
	private int failureStatusCode = HttpServletResponse.SC_FORBIDDEN;
	
	public String getDomainClass() {
		return domainClass;
	}
	public void setDomainClass(String domainClass) {
		this.domainClass = domainClass;
	}
	public int getFailureStatusCode() {
		return failureStatusCode;
	}
	public void setFailureStatusCode(int failureStatusCode) {
		this.failureStatusCode = failureStatusCode;
	}
	public String getAuthenticationHeader() {
		return authenticationHeader;
	}
	public void setAuthenticationHeader(String authenticationHeader) {
		this.authenticationHeader = authenticationHeader;
	}
}

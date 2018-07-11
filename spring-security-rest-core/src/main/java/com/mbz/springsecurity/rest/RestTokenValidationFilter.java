package com.mbz.springsecurity.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.mbz.springsecurity.rest.config.SecurityConfigurationProperties;
import com.mbz.springsecurity.rest.token.AccessToken;

public class RestTokenValidationFilter extends GenericFilterBean {

	private static final Logger log = LoggerFactory.getLogger(RestTokenValidationFilter.class);
	
	@Autowired
	@Qualifier("tokenAuthenticationProvider")
	private RestAuthenticationProvider tokenAuthenticationProvider;
	
	@Autowired
	private SecurityConfigurationProperties securityConfigurationProperties;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String tokenValue = servletRequest.getHeader(this.securityConfigurationProperties.getAuthenticationHeader());
		
		if (tokenValue == null) {
			chain.doFilter(request, response);
		}
		
		log.debug("Token found {}", tokenValue);
		
		try {
			AccessToken authenticationRequest = new AccessToken(tokenValue);
			AccessToken authenticationResult = (AccessToken) this.tokenAuthenticationProvider.authenticate(authenticationRequest);
			
			if (authenticationResult.isAuthenticated()) {
				log.debug("Token authenticated. Storing the authentication result in the security context");
	            log.debug("Authentication result: {}", authenticationResult);
	            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
	            
	            log.debug("Continuing the filter chain");
	            chain.doFilter(request, response);
	            
			}
		} catch (AuthenticationException e) {
			((HttpServletResponse) response).setStatus(this.securityConfigurationProperties.getFailureStatusCode());
		}
	}
}

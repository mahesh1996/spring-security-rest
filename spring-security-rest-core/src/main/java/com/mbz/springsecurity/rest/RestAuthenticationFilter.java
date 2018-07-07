package com.mbz.springsecurity.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mbz.springsecurity.rest.config.SecurityConfigurationProperties;
import com.mbz.springsecurity.rest.credentials.CredentialsExtractor;
import com.mbz.springsecurity.rest.token.AccessToken;
import com.mbz.springsecurity.rest.token.generation.TokenGenerator;
import com.mbz.springsecurity.rest.token.rendering.AccessTokenJsonRenderer;
import com.mbz.springsecurity.rest.token.storage.TokenStorageService;


public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger log = LoggerFactory.getLogger(RestAuthenticationFilter.class);
	
	private ApplicationContext applicationContext;
	
	private CredentialsExtractor credentialsExtractor;
	private AuthenticationManager authenticationManager;
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	private TokenGenerator tokenGenerator;
	private AccessTokenJsonRenderer accessTokenJsonRenderer;
	private TokenStorageService tokenStorageService;
	
	private SecurityConfigurationProperties securityConfigurationProperties;
		
	public RestAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, ApplicationContext context) {
		super(requiresAuthenticationRequestMatcher);
		 this.applicationContext = context;
		 this.credentialsExtractor = context.getBean("credentialsExtractor", CredentialsExtractor.class);
		 this.authenticationDetailsSource = context.getBean("authenticationDetailsSource", AuthenticationDetailsSource.class);
		 this.tokenGenerator = context.getBean("tokenGenerator", TokenGenerator.class);
		 this.accessTokenJsonRenderer = context.getBean("tokenRenderer", AccessTokenJsonRenderer.class);
		 this.tokenStorageService = context.getBean("tokenStorageService", TokenStorageService.class);
		 this.securityConfigurationProperties = context.getBean(SecurityConfigurationProperties.class);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
	
		this.authenticationManager = this.applicationContext.getBean(AuthenticationManager.class);
		UsernamePasswordAuthenticationToken authenticationRequest = credentialsExtractor.extractCredentials(request);
		
		// TODO check for empty string or null value
		if (authenticationRequest.getPrincipal() == null || authenticationRequest.getCredentials() == null) {
			log.debug("Username and/or password parameters are missing. Setting status to {}", HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		authenticationRequest.setDetails(authenticationDetailsSource.buildDetails(request));
		
		log.debug("Trying to authenticate request");
		
		Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
		
		if (authenticationResult.isAuthenticated()) {
			log.debug("Request authenticated. Storing the authentication result in the security context");
			
			SecurityContextHolder.getContext().setAuthentication(authenticationResult);
			return authenticationResult;
		}
		
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException, AuthenticationException {
		UserDetails principal = (UserDetails) authResult.getPrincipal();
		AccessToken tokenValue = this.tokenGenerator.generateAccessToken(principal);
		
		// store token
		this.tokenStorageService.storeToken(tokenValue.getAccessToken(), principal);
		
		// return generated token 
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Cache-Control", "no-store");
		response.addHeader("Pragma", "no-cache");
		
		String jsonToken = this.accessTokenJsonRenderer.generateJson(tokenValue);
		
		response.getWriter().write(jsonToken);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.debug("Authentication Failed {}", failed.getMessage());
		log.debug("Setting status code to {}", this.securityConfigurationProperties.getFailureStatusCode());
		response.setStatus(this.securityConfigurationProperties.getFailureStatusCode());
	}
}

package com.mbz.springsecurity.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.mbz.springsecurity.rest.token.AccessToken;
import com.mbz.springsecurity.rest.token.storage.TokenStorageService;

public class RestAuthenticationProvider implements AuthenticationProvider {
	
	private static final Logger log = LoggerFactory.getLogger(RestAuthenticationProvider.class); 
	
	@Autowired
	private TokenStorageService tokenSorageService;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(AccessToken.class, authentication, "Only AccessToken is supported");
		
		AccessToken authenticationRequest = (AccessToken) authentication;
        AccessToken authenticationResult = new AccessToken(authenticationRequest.getAccessToken());
        
        if (authenticationRequest.getAccessToken() != null) {
        	log.debug("Trying to validate token {}", authenticationRequest.getAccessToken());
        	UserDetails userDetails = tokenSorageService.loadUserByToken(authenticationRequest.getAccessToken());
        	authenticationResult = new AccessToken(userDetails, userDetails.getAuthorities(), authenticationRequest.getAccessToken());
        	log.debug("Authentcation result: {}", userDetails);
        }
        
        return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AccessToken.class.isAssignableFrom(authentication);
	}
}

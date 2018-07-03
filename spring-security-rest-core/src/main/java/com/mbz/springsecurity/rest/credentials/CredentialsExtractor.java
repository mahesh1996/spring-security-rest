package com.mbz.springsecurity.rest.credentials;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface CredentialsExtractor {
	UsernamePasswordAuthenticationToken extractCredentials(HttpServletRequest httpServletRequest);
}

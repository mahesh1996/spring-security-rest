package com.mbz.springsecurity.rest.credentials;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class DefaultJsonPayloadCredentialsExtractor extends AbstractJsonPayloadCredentialsExtractor {

	private static final Logger log = LoggerFactory.getLogger(DefaultJsonPayloadCredentialsExtractor.class);
	
	@Override
	public UsernamePasswordAuthenticationToken extractCredentials(HttpServletRequest httpServletRequest) {
		Map<String, Object> jsonBody = getJsonBody(httpServletRequest);
		log.debug("Usernme from request: {}", jsonBody.get("username").toString());
		return new UsernamePasswordAuthenticationToken(jsonBody.get("username"), jsonBody.get("password"));
	}
}
	
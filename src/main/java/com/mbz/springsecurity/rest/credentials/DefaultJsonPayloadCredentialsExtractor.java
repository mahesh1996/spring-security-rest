package com.mbz.springsecurity.rest.credentials;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class DefaultJsonPayloadCredentialsExtractor extends AbstractJsonPayloadCredentialsExtractor {

	@Override
	public UsernamePasswordAuthenticationToken extractCredentials(HttpServletRequest httpServletRequest) {
		Map<String, Object> jsonBody = getJsonBody(httpServletRequest);

		return new UsernamePasswordAuthenticationToken(jsonBody.get("username"), jsonBody.get("password"));
	}

}

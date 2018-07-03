package com.mbz.springsecurity.rest.credentials;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.mbz.springsecurity.rest.RestAuthenticationFilter;

public abstract class AbstractJsonPayloadCredentialsExtractor implements CredentialsExtractor {
	
	private static final Logger log = LoggerFactory.getLogger(RestAuthenticationFilter.class);
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	Map<String, Object> getJsonBody(HttpServletRequest httpServletRequest) {
		try {
			String body = CharStreams.toString(httpServletRequest.getReader());
			return objectMapper.readValue(body, Map.class);
		} catch (Exception e) {
			return new HashMap<>();
		}
	}
}

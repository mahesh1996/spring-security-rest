package com.mbz.springsecurity.rest.credentials;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.json.GsonJsonParser;

import com.google.common.io.CharStreams;

public abstract class AbstractJsonPayloadCredentialsExtractor implements CredentialsExtractor {

	Map<String, Object> getJsonBody(HttpServletRequest httpServletRequest) {
		try {
			String body = CharStreams.toString(httpServletRequest.getReader());
			GsonJsonParser gsonJsonParser = new GsonJsonParser();
			return gsonJsonParser.parseMap(body);
		} catch (Exception e) {
			return new HashMap<>();
		}
	}
}

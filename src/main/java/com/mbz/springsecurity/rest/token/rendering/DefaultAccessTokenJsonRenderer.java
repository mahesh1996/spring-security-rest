package com.mbz.springsecurity.rest.token.rendering;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbz.springsecurity.rest.token.AccessToken;

@Component("tokenJsonRenderer")
public class DefaultAccessTokenJsonRenderer implements AccessTokenJsonRenderer {

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public String generateJson(AccessToken accessToken) {
		Assert.isInstanceOf(UserDetails.class, accessToken.getPrincipal(), "A UserDetails implementation is required");
		
		UserDetails userDetails = (UserDetails) accessToken.getPrincipal();
		Map<String, Object> result = new HashMap<>();
		
		result.put("username", userDetails.getUsername());
		result.put("token", accessToken.getAccessToken());
		String[] authorities = userDetails.getAuthorities()
				.stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority())
				.toArray(size -> new String[size]);
		result.put("roles", authorities);
		
		try {
			return objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			return "";
		}
	}

}

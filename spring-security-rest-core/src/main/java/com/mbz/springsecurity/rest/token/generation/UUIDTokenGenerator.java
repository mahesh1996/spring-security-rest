package com.mbz.springsecurity.rest.token.generation;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import com.mbz.springsecurity.rest.token.AccessToken;

public class UUIDTokenGenerator implements TokenGenerator {

	@Override
	public AccessToken generateAccessToken(UserDetails principal) {
		return new AccessToken(principal, principal.getAuthorities(), UUID.randomUUID().toString().replaceAll("-", ""));
	}

}

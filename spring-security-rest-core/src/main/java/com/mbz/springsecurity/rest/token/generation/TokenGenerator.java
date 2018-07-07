package com.mbz.springsecurity.rest.token.generation;

import org.springframework.security.core.userdetails.UserDetails;

import com.mbz.springsecurity.rest.token.AccessToken;

public interface TokenGenerator {
	AccessToken generateAccessToken(UserDetails principal);
}

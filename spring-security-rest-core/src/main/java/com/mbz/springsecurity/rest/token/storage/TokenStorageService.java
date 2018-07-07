package com.mbz.springsecurity.rest.token.storage;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenStorageService {
	UserDetails loadUserByToken (String tokenValue) throws TokenNotFoundException;
	void storeToken(String tokenValue, UserDetails userDetails);
}

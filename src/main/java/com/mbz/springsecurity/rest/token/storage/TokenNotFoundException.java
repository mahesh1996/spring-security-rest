package com.mbz.springsecurity.rest.token.storage;

import org.springframework.security.core.AuthenticationException;

public class TokenNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public TokenNotFoundException(String msg) {
		super(msg);
	}
	
}

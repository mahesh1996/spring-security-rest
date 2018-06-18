package com.mbz.springsecurity.rest.token.generation;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.mbz.springsecurity.rest.token.AccessToken;

public class SecureRandomTokenGenerator implements TokenGenerator {

	SecureRandom random = new SecureRandom();
			
	@Override
	public AccessToken generateAccessToken(UserDetails principal) {
		String token = new BigInteger(160, this.random).toString(32);
        int tokenSize = token.length();
        
        if (tokenSize < 32) token += RandomStringUtils.randomAlphanumeric(32 - tokenSize);
        return new AccessToken(principal, principal.getAuthorities(), token);
	}

}

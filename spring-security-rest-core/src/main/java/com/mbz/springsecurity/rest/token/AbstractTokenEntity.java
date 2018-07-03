package com.mbz.springsecurity.rest.token;


public interface AbstractTokenEntity {
	String getToken();
	String getUsername();
	void setToken(String token);
	void setUsername(String username);
}

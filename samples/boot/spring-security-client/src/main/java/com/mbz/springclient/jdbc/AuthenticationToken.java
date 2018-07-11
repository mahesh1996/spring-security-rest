package com.mbz.springclient.jdbc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.mbz.springsecurity.rest.token.AbstractTokenEntity;

@Entity
public class AuthenticationToken implements AbstractTokenEntity {

	@Id
	private String token;
	
	@Column(nullable = false)
	private String username;
	@Override
	public String getToken() {
		return token;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}
}

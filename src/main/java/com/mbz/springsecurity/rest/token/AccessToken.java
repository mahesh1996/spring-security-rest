package com.mbz.springsecurity.rest.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AccessToken extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = -7991137004541158619L;
	private String accessToken;
	private UserDetails principal;
	
	public AccessToken(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		super.setAuthenticated(true);
		// TODO Auto-generated constructor stub
	}
	
	public AccessToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities, String accessToken) {
		this(authorities);
		this.principal = principal;
		this.accessToken = accessToken;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public String getAccessToken() {
		return this.accessToken;
	}
}

package com.mbz.springsecurity.rest.token.rendering;

import com.mbz.springsecurity.rest.token.AccessToken;

public interface AccessTokenJsonRenderer {
	String generateJson(AccessToken accessToken);
}

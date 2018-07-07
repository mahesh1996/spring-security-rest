package com.mbz.springsecurity.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mbz.springsecurity.rest.RestAuthenticationFilter;
import com.mbz.springsecurity.rest.RestTokenValidationFilter;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	private SecurityConfigurationProperties securityConfigurationProperties;
	
	@Autowired
	RestTokenValidationFilter restTokenValidationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, securityConfigurationProperties.getLoginUrl())
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(new RestAuthenticationFilter(new AntPathRequestMatcher(securityConfigurationProperties.getLoginUrl(), "POST"), context), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(restTokenValidationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

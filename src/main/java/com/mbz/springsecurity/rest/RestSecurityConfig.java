package com.mbz.springsecurity.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	RestTokenValidationFilter restTokenValidationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/login")
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(new RestAuthenticationFilter(new AntPathRequestMatcher("/api/login", "POST"), context), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(restTokenValidationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

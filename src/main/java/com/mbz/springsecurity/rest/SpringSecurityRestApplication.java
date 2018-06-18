package com.mbz.springsecurity.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;

@SpringBootApplication
public class SpringSecurityRestApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityRestApplication.class, args);
	}
}

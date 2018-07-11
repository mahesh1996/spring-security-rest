package com.mbz.springsecurity.rest.jdbc;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.mbz.springsecurity.rest.config.SecurityConfigurationProperties;
import com.mbz.springsecurity.rest.token.AbstractTokenEntity;
import com.mbz.springsecurity.rest.token.storage.TokenNotFoundException;
import com.mbz.springsecurity.rest.token.storage.TokenStorageService;

public class JpaTokenStorageService implements TokenStorageService {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private SecurityConfigurationProperties securityConfigurationProperties;
	
	@Override
	public UserDetails loadUserByToken(String tokenValue) throws TokenNotFoundException {
		CrudRepository<AbstractTokenEntity, String> crudRepository = this.applicationContext.getBean("tokenRepository", CrudRepository.class);
		Optional<AbstractTokenEntity> token =  crudRepository.findById(tokenValue);
		
		if (!token.isPresent()) {
			throw new TokenNotFoundException("Token " + tokenValue + "not found");
		}
		
		UserDetailsService userDetailsService = this.applicationContext.getBean(UserDetailsService.class);
		return userDetailsService.loadUserByUsername(token.get().getUsername());
		
	}

	@Override
	public void storeToken(String tokenValue, UserDetails userDetails) {
		AbstractTokenEntity token;
		try {
			token = (AbstractTokenEntity) Class.forName(this.securityConfigurationProperties.getDomainClass()).newInstance();
			token.setToken(tokenValue);
			token.setUsername(userDetails.getUsername());
			
			CrudRepository<AbstractTokenEntity, String> crudRepository = this.applicationContext.getBean("tokenRepository", CrudRepository.class);
			crudRepository.save(token);
			
			// TODO handle all exceptions appropriately
		} catch (InstantiationException e) {
			throw new TokenNotFoundException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new TokenNotFoundException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new TokenNotFoundException(e.getMessage());
		}

	}

}

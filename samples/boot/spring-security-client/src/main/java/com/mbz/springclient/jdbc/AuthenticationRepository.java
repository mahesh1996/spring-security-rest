package com.mbz.springclient.jdbc;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("tokenRepository")
public interface AuthenticationRepository extends CrudRepository<AuthenticationToken, String> {
	AuthenticationToken save(AuthenticationToken token);
	Optional<AuthenticationToken> findById(String token);
}

package com.mbz.springsecurity.rest.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mbz.springsecurity.rest.config.BeanConfiguration;
import com.mbz.springsecurity.rest.config.RestSecurityConfig;
import com.mbz.springsecurity.rest.config.SecurityConfigurationProperties;
import com.mbz.springsecurity.rest.credentials.CredentialsExtractor;
import com.mbz.springsecurity.rest.credentials.DefaultJsonPayloadCredentialsExtractor;
import com.mbz.springsecurity.rest.token.generation.SecureRandomTokenGenerator;
import com.mbz.springsecurity.rest.token.generation.TokenGenerator;
import com.mbz.springsecurity.rest.token.rendering.AccessTokenJsonRenderer;
import com.mbz.springsecurity.rest.token.rendering.DefaultAccessTokenJsonRenderer;
import com.mbz.springsecurity.rest.token.storage.TokenStorageService;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({
	SpringSecurityRestConfiguration.class,
	BeanConfiguration.class,
	RestSecurityConfig.class,
	SecurityConfigurationProperties.class
})
@Documented
@Configuration
public @interface EnableSpringSecurityRestConfiguration {
	Class<? extends CredentialsExtractor> credentialsExtractor() default DefaultJsonPayloadCredentialsExtractor.class;
	Class<? extends TokenGenerator> tokenGenerator() default SecureRandomTokenGenerator.class;
	Class<? extends AccessTokenJsonRenderer> tokenRenderer() default DefaultAccessTokenJsonRenderer.class;
	Class<? extends TokenStorageService> tokenStorageService() default TokenStorageService.class;
}

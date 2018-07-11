package com.mbz.springsecurity.rest.config.annotation;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.mbz.springsecurity.rest.credentials.CredentialsExtractor;
import com.mbz.springsecurity.rest.token.generation.TokenGenerator;
import com.mbz.springsecurity.rest.token.rendering.AccessTokenJsonRenderer;
import com.mbz.springsecurity.rest.token.storage.TokenStorageService;

@Configuration
public class SpringSecurityRestConfiguration implements ImportAware, ApplicationContextAware {
	
	private GenericApplicationContext applicationContext;
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> attributeMap = importMetadata
				.getAnnotationAttributes(EnableSpringSecurityRestConfiguration.class.getName());
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(attributeMap);

		Class<? extends CredentialsExtractor> credentialsExtractor = attributes.getClass("credentialsExtractor");
		this.registerBean("credentialsExtractor", credentialsExtractor);
		
		Class<? extends TokenGenerator> tokenGenerator = attributes.getClass("tokenGenerator");
		this.registerBean("tokenGenerator", tokenGenerator);
		
		Class<? extends AccessTokenJsonRenderer> tokenRenderer = attributes.getClass("tokenRenderer");
		this.registerBean("tokenRenderer", tokenRenderer);
		
		Class<? extends TokenStorageService> tokenStorageService = attributes.getClass("tokenStorageService");
		
		if (!tokenStorageService.getName().equals(TokenStorageService.class.getName())) {
			this.registerBean("tokenStorageService", tokenStorageService);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (GenericApplicationContext) applicationContext;
	}

	private void registerBean(String beanName, Class beanClass) {
		BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
		this.applicationContext.registerBeanDefinition(beanName, beanDefinition);
	}
}

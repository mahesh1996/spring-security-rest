package com.mbz.springsecurity.rest.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Documented
@Import(JdbcConfiguration.class)
@Configuration
public @interface EnableJdbcConfiguration {

}

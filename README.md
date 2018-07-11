# spring-security-rest
Library to secure your spring based rest apis in just a second.

> * Note: This is currently in active development.

## How to use it? (Sorry for docs. This will be shortly migrated to github pages)

* Create new spring project. (Boilerplate can be easily generated from [here](https://start.spring.io/))
* Add following dependencies to pom.xml OR build.gradle accordingly. (**Below example has been included in `samples` directory**)

    * ```
        <dependency>
		    <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
		    <groupId>com.h2database</groupId>
		    <artifactId>h2</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mbz</groupId>
			<artifactId>spring-security-rest-jdbc</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>

    * You can add your choice of database. For demo purpose, we are going to use `h2` in-memory database.

* Add Configuration file in your app as follows,

    * ```
        import com.mbz.springsecurity.rest.jdbc.EnableJdbcConfiguration;
        import com.mbz.springsecurity.rest.config.annotation.EnableSpringSecurityRestConfiguration;

        @EnableJdbcConfiguration
        @EnableSpringSecurityRestConfiguration
        @Configuration
        @EnableWebSecurity
        public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

            // Provide authentication manager which will be used for authenticating user while generating token.
            // This is in memory authentication manager
            // You can provide DaoAuthenticationProvider or UserDetailsService directly (In most scenario, userDetailsService is provided.) [Check For more info](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#jc-authentication-userdetailsservice)

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
            }

            // This is required when using in memory user details service otherwise default password encode is BCryptPasswordEncoder as of spring 5.
            @Bean
            public PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
            }

            // Provide standard user details service implementation which will load user from database by username
            // Using in memory user details service right now.
            @Bean("userDetailsService")
            public UserDetailsService userDetailsService() {
                InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
                manager.createUser(
                        User.withUsername("user").password("password").roles("USER").build());
                return manager;
            }

            // Make authenticationManager available to ApplicationContext so that our library can access it.
            @Bean("authenticationManager")
            @Override
            public AuthenticationManager authenticationManagerBean() throws Exception {
                return super.authenticationManagerBean();
            }

* Add Hello World RestController
    * ```
        @RestController
        public class HelloController {
            @GetMapping("/hello")
            public String hello() {
                return "hello";
            }
        }

* Add Domain class for storing Authentication Token in your app as follows.
    * ```
        // Other import statements
        import com.mbz.springsecurity.rest.token.AbstractTokenEntity;

        @Entity
        public class AuthenticationToken implements AbstractTokenEntity {

            @Id
            private String token;

            @Column(nullable = false)
            private String username;
            @Override
            public String getToken() {
                return token;
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public void setToken(String token) {
                this.token = token;
            }

            @Override
            public void setUsername(String username) {
                this.username = username;
            }
        }

* Expose Repository to be used by library as follows
    * ```
        @Repository("tokenRepository")
        public interface AuthenticationRepository extends CrudRepository<AuthenticationToken, String> {
            AuthenticationToken save(AuthenticationToken token);
            Optional<AuthenticationToken> findById(String token);
        }

* Configure data source in `application.properties` file
    * ```
        spring.h2.console.enabled=true
        spring.h2.console.path=/h2_console
        spring.datasource.url=jdbc:h2:mem:SecurityRest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
        spring.datasource.username=sa
        spring.datasource.password=
        spring.datasource.driverClassName=org.h2.Driver
        spring.jpa.hibernate.ddl-auto = update
        spring.jpa.show-sql=true
        spring.security.rest.domain-class=com.mbz.springclient.jdbc.AuthenticationToken // Domain class name which will be used for storing token into database
        spring.security.rest.failure-status-code=403 // Status code to be returned when token validation fails (default is 403)
        spring.security.rest.authentication-header=X-Auth-Token // header name in which to send token (default is X-Auth-Token)
        spring.security.rest.login-url=/api/login // url name from which token can be generated by sending `username` and `password` in body in POST request
        logging.level.com.mbz.springsecurity.rest = DEBUG // Logging level to be used in library

* Boot the app using `./mvnw spring-boot:run`
* Hit the `/api/login` (Endpoint which you've provided in application.properties file) along with username and pasword
    * e.g. In above case using `httpie` rest client (`http POST :8080/api/login username=user password=pass`)
    * It will return following response
        * ```
            HTTP/1.1 200
            Cache-Control: no-store
            Content-Length: 84
            Content-Type: application/json;charset=UTF-8
            Date: Sat, 07 Jul 2018 08:12:47 GMT
            Pragma: no-cache
            Set-Cookie: JSESSIONID=C680B4CDA794A99A87D6C7014CDF34F9; Path=/; HttpOnly
            X-Content-Type-Options: nosniff
            X-Frame-Options: DENY
            X-XSS-Protection: 1; mode=block

            {
                "roles": [
                    "ROLE_USER"
                ],
                "token": "jb8ia1hn1lt761admtcab6cqa556l7ta",
                "username": "user"
            }
* Hit protected endpoint by setting above returned token into header `X-Auth-Token` (or whatever you've configured in application.properties file)
    * `http GET :8080/hello X-Auth-Token:jb8ia1hn1lt761admtcab6cqa556l7ta`
    * You will get following response if token is correct
    * ```
        HTTP/1.1 200
        Cache-Control: no-cache, no-store, max-age=0, must-revalidate
        Content-Length: 5
        Content-Type: text/plain;charset=UTF-8
        Date: Sat, 07 Jul 2018 08:14:27 GMT
        Expires: 0
        Pragma: no-cache
        Set-Cookie: JSESSIONID=C66E06EC704376E163A114DEAB09BA58; Path=/; HttpOnly
        X-Content-Type-Options: nosniff
        X-Frame-Options: DENY
        X-XSS-Protection: 1; mode=block

        hello
    * Or following response if token is incorrect.
    * ```
        HTTP/1.1 403
        Cache-Control: no-cache, no-store, max-age=0, must-revalidate
        Content-Length: 0
        Date: Sat, 07 Jul 2018 08:14:20 GMT
        Expires: 0
        Pragma: no-cache
        X-Content-Type-Options: nosniff
        X-Frame-Options: DENY
        X-XSS-Protection: 1; mode=block

## Using custom token generation, rendering, credentials extracting strategy

### Custom Token generation strategy
* Currently Library provides two token generation strategy. (More to come in future)
    1. SecureRandomTokenGenerator (Default one)
    2. UUIDTokenGenerator
* You can specify which token generation strategy to user by specifying `tokenGenerator` attribute of `@EnableSpringSecurityRestConfiguration` annotation as follows.
    * ```
        @EnableSpringSecurityRestConfiguration(tokenGenerator = UUIDTokenGenerator.class)
    * You can also create custom Token Generation Strategy by implementing `TokenGenerator` interface and specifying implementing class into `tokenGenerator` attribute of annotation.

### Custom Credentials Extractor
* Credentials Extractor is used to extract credentials from request while generating token.
* Default credentials extractor is `DefaultJsonPayloadCredentialsExtractor` which extracts user name form `username` field of request body and password from `password`.
* You can create custom credentials extractor by implementing `CredentialsExtractor` interface and specifying implementing class in `credentialsExtractor` attribute of `@EnableSpringSecurityRestConfiguration` annotation

### Custom Token Renderer
* Token renderer decides what would be returned in response on successful authentication.
* Default Token Renderer is `DefaultAccessTokenJsonRenderer` which renders following field as JSON.
    * username (string)
    * token (string)
    * roles (array of granted authorities of current user)
* You can create token renderer by implementing `AccessTokenJsonRenderer` interface and specifying implementing class in `tokenRenderer` attribute of `@EnableSpringSecurityRestConfiguration` annotation

## Note
* This library currently supports JDBC Storage service. in Future, Redis, Memcached will be added.

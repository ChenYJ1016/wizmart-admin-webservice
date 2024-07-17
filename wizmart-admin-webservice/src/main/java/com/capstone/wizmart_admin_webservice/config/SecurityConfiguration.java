package com.capstone.wizmart_admin_webservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.capstone.wizmart_admin_webservice.handlers.CustomLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Value("${aws.cognito.logoutUrl}")
	private String logoutUrl;

	@Value("${aws.cognito.logout.success.redirectUrl}")
	private String logoutRedirectUrl;

	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String clientId;

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("continue");

        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/admin", "/logout").authenticated() // Require authentication for /admin
                    .requestMatchers("/").permitAll() // Public access for /
            )
            .oauth2Login(oauth2Login ->
                oauth2Login
                    .loginPage("/oauth2/authorization/cognito") // Redirect to Cognito for login
                    .defaultSuccessUrl("/landingPage") // Redirect after successful login
                    .userInfoEndpoint(userInfoEndpoint ->
                        userInfoEndpoint
                            .oidcUserService(oidcUserService())
                    )
            )
            .logout(httpSecurityLogoutConfigurer -> {
                httpSecurityLogoutConfigurer.logoutSuccessHandler(
                    (LogoutSuccessHandler) new CustomLogoutHandler(logoutUrl, logoutRedirectUrl, clientId));
              })
            .requestCache(cache ->
                cache.requestCache(requestCache)
            );

        return http.build();
    }
}

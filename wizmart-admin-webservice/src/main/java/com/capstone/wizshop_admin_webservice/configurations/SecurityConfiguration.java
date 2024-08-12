package com.capstone.wizshop_admin_webservice.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.capstone.wizshop_admin_webservice.handlers.CustomLogoutHandler;
import com.capstone.wizshop_admin_webservice.properties.Properties;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private Properties properties;

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String logoutUrl = properties.getLogoutUrl();
        String logoutRedirectUrl = properties.getLogoutSuccessRedirectUrl();
        String clientId = properties.getClientId();

        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("continue");

        http
            .csrf(csrf -> csrf 
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/admin/**").authenticated()
                    .requestMatchers("static/**").permitAll()
            )
            .oauth2Login(oauth2Login ->
                oauth2Login
                    .loginPage("/oauth2/authorization/cognito")
                    .defaultSuccessUrl("/admin", true)
                    .userInfoEndpoint(userInfoEndpoint ->
                        userInfoEndpoint.oidcUserService(oidcUserService())
                    )
            )
            .logout(httpSecurityLogoutConfigurer ->
                httpSecurityLogoutConfigurer.logoutUrl("/admin/logout").logoutSuccessHandler(
                    (LogoutSuccessHandler) new CustomLogoutHandler(logoutUrl, logoutRedirectUrl, clientId)
                )
            )
            .requestCache(cache -> cache.requestCache(requestCache))
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .accessDeniedPage("/error")
            );

        return http.build();
    }
}

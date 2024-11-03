// src/main/java/com/capstone/wizshop_admin_webservice/configurations/SecurityConfiguration.java

package com.capstone.wizshop_admin_webservice.configurations;

import com.capstone.wizshop_admin_webservice.filters.JwtAuthenticationFilter;
import com.capstone.wizshop_admin_webservice.properties.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private Properties properties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf // Ensure CSRF protection is enabled
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/auth/**", "/static/**", "/health").permitAll()
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout") // This URL triggers the logout
                .logoutSuccessUrl("/auth/login") // Redirect here after logout
                .invalidateHttpSession(true) // Invalidate session on logout
                .deleteCookies("JSESSIONID") // Clear cookies if needed
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(properties.getJwtSecret());
    }
}

// src/main/java/com/capstone/wizshop_admin_webservice/filters/JwtAuthenticationFilter.java

package com.capstone.wizshop_admin_webservice.filters;

import com.capstone.wizshop_admin_webservice.Services.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(String secretKey) {
        this.tokenService = new TokenService(secretKey);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        String token = null;

        // Try to get token from session
        if (request.getSession() != null) {
            token = (String) request.getSession().getAttribute("token");
        }

        if (token != null) {
            try {
                Claims claims = tokenService.parseToken(token);
                // Extract username and roles from claims
                String username = claims.getSubject();
                // You can also extract roles if you included them in the token
                // List<GrantedAuthority> authorities = getAuthoritiesFromClaims(claims);

                // Create authentication token
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                // Set authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                request.setAttribute("token", token);
                request.setAttribute("claims", claims);
            } catch (Exception e) {
                // Invalid token
                logger.error("Invalid token", e);
                response.sendRedirect("/auth/login");
                return;
            }
        } else {
            // No token, redirect to login if accessing protected resources
            if (requiresAuthentication(request)) {
                response.sendRedirect("/auth/login");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/admin");
    }
}

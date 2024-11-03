package com.capstone.wizshop_admin_webservice.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    @Value("${wizshop.common.repo.url}")
    private String commonRepoUrl;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    // Getters
    public String getCommonRepoUrl() {
        return commonRepoUrl;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }
}

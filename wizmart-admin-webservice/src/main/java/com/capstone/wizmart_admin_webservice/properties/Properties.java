package com.capstone.wizmart_admin_webservice.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	
	 @Value("${aws.cognito.logoutUrl}")
	 private String logoutUrl;

	 @Value("${aws.cognito.logout.success.redirectUrl}")
	 private String logoutSuccessRedirectUrl;
	 
	 @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	 private String clientId;

	 public String getLogoutUrl() {
        return logoutUrl;
	 }

	 public String getLogoutSuccessRedirectUrl() {
        return logoutSuccessRedirectUrl;
	 }
	 
	 public String getClientId() {
		 return clientId;
	 }
}

package com.capstone.wizmart_admin_webservice.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {
	
	 @Value("${aws.cognito.logoutUrl}")
	 private String logoutUrl;

	 @Value("${aws.cognito.logout.success.redirectUrl}")
	 private String logoutSuccessRedirectUrl;

	 public String getLogoutUrl() {
        return logoutUrl;
	 }

	 public String getLogoutSuccessRedirectUrl() {
        return logoutSuccessRedirectUrl;
	 }
}

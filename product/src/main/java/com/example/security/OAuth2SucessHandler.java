package com.example.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.model.Provider;
import com.example.model.User;
import com.example.service.UserService;

@Component
public class OAuth2SucessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserService userService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		CustomOAuth2User oauth2User=(CustomOAuth2User) authentication.getPrincipal();
		String email=oauth2User.getEmail();
		String name=oauth2User.getName();
		User user=userService.getUserByUserEmail(email);
		if(user == null)
		{
			userService.registerNewCustomerAfterOAuth2Login(email,name,Provider.FACEBOOK);
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

	
}

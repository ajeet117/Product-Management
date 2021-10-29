package com.example.utility;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.model.User;
import com.example.security.MyUserDetails;
import com.example.service.UserService;

@Component
public class CustomLoginSucessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserService service;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		MyUserDetails userDetails=(MyUserDetails) authentication.getPrincipal();
		User user=userDetails.getUser();
		if(user.getFailedAttempt() > 0)
		{
			service.resetFailedAttempts(user);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

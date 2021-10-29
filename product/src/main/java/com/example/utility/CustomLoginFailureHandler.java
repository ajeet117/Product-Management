package com.example.utility;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository repo;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String email=request.getParameter("email");
		User user=repo.getUserByEmail(email);
		
		if(user != null)
		{
			if(user.isAccountNonLocked())
			{
				if(user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS-1)
				{
					userService.increaseFailedAttempts(user);
					System.out.println("YO what's up");
				}
				else
				{
					userService.lock(user);
					System.out.println("user locked");
					exception=new LockedException("Your account have been locked due to many failed Attempt." + " it will be unlocked after 10 minutes");
				}
			}
			else if(!user.isAccountNonLocked())
			{
				if(userService.unlockWhenTimeExpired(user))
				{
					exception=new LockedException("Your account has been unlocked. Try again to login ");
					
				}
			}
			
		}
		super.setDefaultFailureUrl("/login?error");
		super.onAuthenticationFailure(request, response, exception);
	}
}

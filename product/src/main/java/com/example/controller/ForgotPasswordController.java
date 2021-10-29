package com.example.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.example.model.User;
import com.example.service.UserNotFoundException;
import com.example.service.UserService;
import com.example.utility.Utility;

import net.bytebuddy.utility.RandomString;



@Controller
public class ForgotPasswordController {

	@Autowired
	private UserService userService;
	@Autowired
	private JavaMailSender mailSender;
	@GetMapping("/forgotpassword")
	public String forgotPassword()
	{
		return "forgotpassword";
	}
	
	@PostMapping(value="/forgotpassword")
	public String processForgetPasswod(HttpServletRequest request,Model model)
	{
		String email=request.getParameter("email");
		System.out.println(email);
		String token=RandomString.make(30);
		
		 try {
		        userService.updateResetPasswordToken(token, email);
		        String resetPasswordLink = Utility.getSiteURL(request) + "/resetpassword?token=" + token;
		        sendEmail(email, resetPasswordLink);
		        model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
		         
		    } catch (UserNotFoundException ex) {
		        model.addAttribute("error", ex.getMessage());
		    } catch (UnsupportedEncodingException | MessagingException e) {
		    	 model.addAttribute("error", "Error while sending email");
		    }
		     
		return "forgotpassword";
	}
	public void sendEmail(String recipientEmail,String link) throws MessagingException, UnsupportedEncodingException{
		
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom("contact@Product.com","Product support");
		helper.setTo(recipientEmail);
		  String subject = "Here's the link to reset your password";
		     
		    String content = "<p>Hello,</p>"
		            + "<p>You have requested to reset your password.</p>"
		            + "<p>Click the link below to change your password:</p>"
		            + "<p><a href=\"" + link + "\">Change my password</a></p>"
		            + "<br>"
		            + "<p>Ignore this email if you do remember your password, "
		            + "or you have not made the request.</p>";
		     
		    helper.setSubject(subject);
		     
		    helper.setText(content, true);
		     
		    mailSender.send(message);
		
	}
	
	@GetMapping("/resetpassword")
	public String showResetPasswordForm(@Param(value="token") String token, Model model)
	{
		User user=userService.getByResetPasswordToken(token);
		model.addAttribute("token",token);
		
		if(user == null)
		{
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		return "reset_password_form";
		
	}
	
	@PostMapping(value="/resetpassword")
	public String processResetPassowrd(HttpServletRequest request, Model model)
	{
		String token=request.getParameter("token");
		String password=request.getParameter("password");
		
		User user=userService.getByResetPasswordToken(token);
		if(user == null)
		{
			model.addAttribute("message","Invalid Token");
			return "message";
		}
		else
		{
			userService.updateResetPassword(user, password);
			model.addAttribute("message","Your password has been change succesfully ");
		}
		return "message";
	}
}

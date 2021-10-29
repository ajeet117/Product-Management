package com.example.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.security.MyUserDetails;
import com.example.service.UserService;

@Controller
public class UserController {

	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepo;
	@RequestMapping("/user/create")
	public String createUser(Model model)
	{
		List<Role> allRole=roleRepo.findAll();
		model.addAttribute("title","Product-Sign Up");
		model.addAttribute("allRole",allRole);
		model.addAttribute("user",new User());
		return "signup";
	}
	@RequestMapping(value="/user/save",method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user)
	{
		String rawPassword=user.getPassword();
		String encryptPassword=new BCryptPasswordEncoder().encode(rawPassword);
		user.setPassword(encryptPassword);
		user.setAccountNonLocked(true);
		userService.save(user);
		return "redirect:/login";
	}
	@RequestMapping(value="/user/view",method = RequestMethod.GET)
	public String viewUser(Model model)
	{
		return viewPage(1,model);
	}
	@RequestMapping(value="/user/page/{pagenum}")
	public String viewPage(@PathVariable(name="pagenum") int pagenum,Model model)
	{
		Page<User> page=userService.listAll(pagenum);
		List<User> allUser=page.getContent();
		model.addAttribute("currentpage",pagenum);
		model.addAttribute("totalpage",page.getTotalPages());
		model.addAttribute("totalitems",page.getTotalElements());
		model.addAttribute("allUser",allUser);
		return "view_user";
		
	}
	@RequestMapping(value="/account")
	public String account(@AuthenticationPrincipal MyUserDetails user,Model model)
	{
		String email=user.getUsername();
		User savedUser=userService.getUserByUserEmail(email);
		model.addAttribute("title","Edit-User");
		model.addAttribute("user",savedUser);
		return "edit_user";
	}
	@RequestMapping(value="/account/save",method = RequestMethod.POST)
	public String updateAccount(@AuthenticationPrincipal MyUserDetails loggedUser,@ModelAttribute("user") User user,RedirectAttributes ra)
	{
		String firstName=user.getFirstName();
		String lastName=user.getLastName();
		String address=user.getAddress();
		String dob=user.getDob();
		String gender=user.getGender();
		String email=user.getEmail();
		userService.updateAccount(firstName, lastName, address, dob, gender, email);
		loggedUser.setFirstName(firstName);
		loggedUser.setLastName(lastName);
		
		ra.addFlashAttribute("message","Account has been updated");
		return "redirect:/account";
	}
	@RequestMapping(value="/setting")
	public String setting(Model model)
	{
		model.addAttribute("title","User-Setting");
		return "change_password";
	}
	@RequestMapping(value="/changepassword",method = RequestMethod.POST)
	public String changePassword(@AuthenticationPrincipal MyUserDetails user,RedirectAttributes ra,HttpServletRequest request)
	{
		String email=user.getUsername();
		User loggedUser=userService.getPassword(email);
		String currentPassword=request.getParameter("currentpassword");
		String newPassword=request.getParameter("newpassword");
		boolean matches=new BCryptPasswordEncoder().matches(currentPassword, loggedUser.getPassword());
		if(matches)
		{
			String encodedPassword=new BCryptPasswordEncoder().encode(newPassword);
			userService.updatePassword(encodedPassword, email);
			ra.addFlashAttribute("message","Password has been updated");
		}
		else
		{
			ra.addFlashAttribute("error","current password doesnot matches");
		}
		return "redirect:/setting";
	}
	
	
}

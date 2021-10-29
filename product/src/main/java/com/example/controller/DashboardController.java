package com.example.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.BrandService;
import com.example.service.ProductService;
import com.example.service.UserService;

@Controller
public class DashboardController {

	@Autowired
	private BrandService service;
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	@RequestMapping("/")
	public String homePage(Model model)
	{
		int allBrand=service.findAll().size();
		int allProduct=productService.findAll().size();
		int allUser=userService.findAll().size();
		
		model.addAttribute("allBrand",allBrand);
		model.addAttribute("allProduct",allProduct);
		model.addAttribute("allUser",allUser);
		model.addAttribute("title","Dashbord");
		return "index";
	}
	 @GetMapping("/login")
	    public String showLoginForm(Model model) {
	         
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	            return "login";
	        }
	 
	        return "redirect:/";
	    }
	
}

package com.example.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.model.Role;
import com.example.model.User;

public class MyUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	

	public MyUserDetails(User user) {
		
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> role=user.getRole();
		List<SimpleGrantedAuthority> authority=new ArrayList<>();
		for(Role r:role )
		{
			authority.add(new SimpleGrantedAuthority(r.getName()));
		}
		return authority;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


	public User getUser()
	{
		return this.user;
	}
	
	public void setFirstName(String firstName)
	{
		this.user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName)
	{
		this.user.setLastName(lastName);
	}
	public void setEmail(String email)
	{
		this.user.setEmail(email);
	}
	public void setAddress(String address)
	{
		this.user.setAddress(address);
	}
	public void setDob(String dob)
	{
		this.user.setDob(dob);
	}
	public void setGender(String gender)
	{
		this.user.setGender(gender);
	}
	public String getFullName()
	{
		return this.user.getFirstName() + " " +this.user.getLastName();
	}
	

}

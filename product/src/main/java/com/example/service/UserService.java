package com.example.service;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Provider;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
@Transactional
public class UserService {

	public static final int MAX_FAILED_ATTEMPTS = 3;
	private static final long LOCK_TIME = 10*60*1000;
	
	@Autowired
	private UserRepository userRepo;
	
	public List<User> findAll()
	{
		return userRepo.findAll();
	}
	public Page<User> listAll(int pageNum)
	{
		int pageSize=5;
		Pageable pageable=PageRequest.of(pageNum - 1, pageSize);
		 return userRepo.findAll(pageable);
	}
	
	public void save(User user)
	{
		userRepo.save(user);
	}
	public User getUserByUserEmail(String email)
	{
		return userRepo.getUserByEmail(email);
	}
	public User findById(int id)
	{
		return userRepo.findById(id).get();
	}
	public User getPassword(String email)
	{
		return userRepo.getPassword(email);
	}
	public void updatePassword(String password,String email)
	{
		userRepo.updatePassword(password, email);
	}
	public void increaseFailedAttempts(User user)
	{
		int newFailAttempts=user.getFailedAttempt() +1;
		userRepo.updateFailedAttempts(newFailAttempts, user.getEmail());
	}
	
	public void resetFailedAttempts(User user)
	{
		userRepo.updateFailedAttempts(0, user.getEmail());
	}
	public void lock(User user)
	{
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		userRepo.save(user);
	}
	public boolean unlockWhenTimeExpired(User user)
	{
		long lockTimeInMills=user.getLockTime().getTime();
		long currentTimeInMills=System.currentTimeMillis();
		
		if(lockTimeInMills + LOCK_TIME < currentTimeInMills)
		{
			user.setAccountNonLocked(true);
			user.setLockTime(null);
			user.setFailedAttempt(0);
			userRepo.save(user);
			return true;
		}
		return false;
	}

	public void registerNewCustomerAfterOAuth2Login(String email, String name, Provider authProvider) {
		User user=new User();
		user.setEmail(email);
		user.setFirstName(name);
		user.setProvider(authProvider);
		userRepo.save(user);
		
	}
	public void updateAccount(String firstName,String lastName,String address, String dob, String gender,String email)
	{
		userRepo.updateAccount(firstName, lastName, address, dob, gender, email);
	}
	
	public void updateResetPasswordToken(String token, String email) throws UserNotFoundException
	{
		User user=userRepo.getUserByEmail(email);
		if(user != null)
		{
			user.setResetPasswordToken(token);
			userRepo.save(user);
		}
		else
		{
			throw new UserNotFoundException("Could not find any User with the email");
		}
	}
	
	public User getByResetPasswordToken(String token)
	{
		return userRepo.findByResetPasswordToken(token);
	}
	public void updateResetPassword(User user,String newPassword)
	{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodePassword=passwordEncoder.encode(newPassword);
		user.setPassword(encodePassword);
		user.setResetPasswordToken(null);
		userRepo.save(user);
	}
	
}

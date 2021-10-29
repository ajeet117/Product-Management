package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	
	public User findByResetPasswordToken(String token);
	
	@Query("Select new User(u.password) from User u where u.email=?1")
	public User getPassword(String email);

	@Query("SELECT u  From User u WHERE u.email=:email")
	public User getUserByEmail(@Param("email")String email);
	
	@Query("Update User u SET u.failedAttempt=?1 WHERE u.email=?2")
	@Modifying
	public void updateFailedAttempts(int failAttempt,String email);
	
	@Query("Update User u SET u.firstName=?1 , u.lastName= ?2 , u.address =?3 , u.dob = ?4, u.gender =?5 WHERE u.email=?6 ")
	@Modifying
	public void updateAccount(String firstName, String lastname,String address, String dob, String gender ,String email);
	
	@Query("Update User u SET u.password=?1 WHERE u.email=?2")
	@Modifying
	public void updatePassword(String password,String email);
}

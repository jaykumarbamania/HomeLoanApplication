package com.homeloan.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.homeloan.app.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	
	Users findUsersByUsernameAndPassword(String username, String password);
	
	Users findUsersByEmail(String email);
	
	Users findUsersByPhone(Long phone);
	
	Users findUsersByUsername(String username);
}

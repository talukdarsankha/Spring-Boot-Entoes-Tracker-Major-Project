package com.xyz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xyz.entity.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {

	public boolean existsByEmail(String em);
	
	public Users findByEmail(String em);
	
	public Users findByEmailAndMobileNumber(String email,String mobileNumber);
	
}

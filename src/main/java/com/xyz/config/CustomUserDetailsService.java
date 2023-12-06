package com.xyz.config;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.xyz.Repository.UserRepo;
import com.xyz.entity.Users;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo ur;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Users us= ur.findByEmail(username);
		if(us!=null) {
			return new CustomUserDetails(us);
		}else {
			throw new UsernameNotFoundException("This email Not Exist!!!");
		}
	}

}

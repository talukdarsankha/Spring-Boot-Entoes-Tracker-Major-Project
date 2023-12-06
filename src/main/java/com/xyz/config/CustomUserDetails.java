package com.xyz.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.xyz.entity.Users;

public class CustomUserDetails implements UserDetails {

	private Users us;
	public CustomUserDetails(Users us) {
		this.us=us;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		SimpleGrantedAuthority sga = new SimpleGrantedAuthority(us.getRole());
		return Arrays.asList(sga);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return us.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return us.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
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
	
	
	
	

}

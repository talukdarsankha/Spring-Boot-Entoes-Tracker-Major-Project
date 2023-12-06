package com.xyz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.security.PermitAll;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder bpe() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private UserDetailsService uds;
	
	@Bean
	public DaoAuthenticationProvider getDap() {
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setUserDetailsService(uds);
		dap.setPasswordEncoder(bpe());
		
		return dap;
	}
	
	@Bean
	public SecurityFilterChain secu(HttpSecurity hhs) throws Exception {
		hhs.csrf().disable().authorizeHttpRequests()
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/**").permitAll()
		.and().formLogin().loginPage("/LoginPage")
		.loginProcessingUrl("/wesd")
		.defaultSuccessUrl("/user/AddNotesPage")
		.permitAll();
		
		return hhs.build(); 
	}

}

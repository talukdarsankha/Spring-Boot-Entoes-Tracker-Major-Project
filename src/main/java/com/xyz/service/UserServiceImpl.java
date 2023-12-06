package com.xyz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xyz.Repository.UserRepo;
import com.xyz.entity.Users;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo ur;
	@Autowired
	private BCryptPasswordEncoder bpe;
	
	
	@Override
	public Users SaveUsers(Users us) {
		// TODO Auto-generated method stub
		us.setRole("ROLE_USER");
		String password= us.getPassword();
		us.setPassword(bpe.encode(password));
		Users users= ur.save(us);
			return users;
	}

	@Override
	public boolean existEmailCheck(String em) {
		// TODO Auto-generated method stub
		return ur.existsByEmail(em);
		
		
	}

	@Override
	public void removeSession() {
		// TODO Auto-generated method stub
	 HttpSession hs= ((ServletRequestAttributes)	(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		hs.removeAttribute("msg");
	}

	public Users checkByEmailAndMobile(String email,String mobile) {
		return ur.findByEmailAndMobileNumber(email, mobile);
	}
	
	
	
}

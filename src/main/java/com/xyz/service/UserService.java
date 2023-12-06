package com.xyz.service;

import com.xyz.entity.Users;

public interface UserService {
	
	public Users SaveUsers(Users us);
	public boolean existEmailCheck(String em);
	public void removeSession();
	public Users checkByEmailAndMobile(String email,String mobile);
}

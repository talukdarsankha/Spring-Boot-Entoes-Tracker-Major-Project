package com.xyz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyz.Repository.UserRepo;
import com.xyz.entity.Users;
import com.xyz.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	
	@Autowired
	private UserService usv;
	@Autowired
	private UserRepo ur;
	@Autowired
	private BCryptPasswordEncoder bpc;
	
	@GetMapping("/BasePage")
	public String getBase() {
		return "Base";
	}
	
	@GetMapping("/")
	public String getIndex() {
		return "index";
	}
	
	@GetMapping("/home")
	public String getHome() {
		return "index";
	}
	
	@GetMapping("/LoginPage")
	public String getLogin() {
		return "Login";
	}
	@GetMapping("/RegisterPage")
	public String getRegister() {
		return "Register";
	}
	
	
	
	@PostMapping("/RegisterUser")
	public String createUser(@ModelAttribute Users us,HttpSession hs) {
		boolean f= usv.existEmailCheck(us.getEmail());
		if(f) {
			hs.setAttribute("msg", "This email already exist!!");
		}else {
			Users u= usv.SaveUsers(us);
			if(u!=null) {
				hs.setAttribute("msg", "You Are Registered Successfully!!!");
				System.out.println(u);
			}else {
				hs.setAttribute("msg", "Something Went Wrong On Server !!!");
			}
		}
		return "redirect:/RegisterPage";
	}
	
	@GetMapping(path = "/forgotPasswordPage")
	public String loadforgotPasswordPage() {
		return "forgotPasswordPage";
	}
	@GetMapping(path = "/resetPasswordPage/{id}")
	public String loadresetPasswordPage(@PathVariable("id") int id,Model m) {
		m.addAttribute("id", id);
		return "resetPasswordPage";
	}
	
	@PostMapping(path = "/forgotPassword")
	public String forgotPassword(@RequestParam("email") String email,@RequestParam("mobile") String mobile,HttpSession hs) {
		Users u= usv.checkByEmailAndMobile(email, mobile);
		if(u!=null) {
			return "redirect:/resetPasswordPage/"+u.getId();
		}else {
			hs.setAttribute("msg", "Email Or Mobile Number May Be Incorrect!!!!");
          return "redirect:/forgotPasswordPage";
		}
	}
	@PostMapping(path = "/resetPassword")
	public String resetPassword(@RequestParam("newPassword") String newPassword,@RequestParam("reEnterPassword") String reEnterPassword,HttpSession hs,@RequestParam("id") int id) {
	Users u= ur.findById(id).get();
		if(newPassword.equals(reEnterPassword)) {
		u.setPassword(newPassword);
		Users us= usv.SaveUsers(u);
		if(us!=null) {
			hs.setAttribute("msg", "Your Password Reset Successfully!!1");
		}else {
			hs.setAttribute("msg", "Something Went Wrong On Server !!!");
			 return "redirect:/resetPasswordPage/"+id;
		}
		 return "redirect:/LoginPage";
	  }else {
		  hs.setAttribute("msg", "Two Password Are Not Same !!!");
		  return "redirect:/resetPasswordPage/"+id; 
	  }
		
	}
	

}

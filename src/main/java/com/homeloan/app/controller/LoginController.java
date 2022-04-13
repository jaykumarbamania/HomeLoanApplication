package com.homeloan.app.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.homeloan.app.model.Users;
import com.homeloan.app.service.UserService;


@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	


//	@RequestMapping(value = "/showLogin", method = RequestMethod.GET)
	@GetMapping("/login")
	public ModelAndView getLoginPage(Model model) {
		Users users = new Users();
		String viewName = "login";
		String modelName = "user";
		model.addAttribute("loggedIn",null);
		return new ModelAndView(viewName, modelName, users);
		
	}
	
	@PostMapping("/checkLogin")
	public String checkUser(@RequestParam("username") String username,
							@RequestParam("password") String password,
							HttpSession session,Model model) {
		
		Users admin = userService.findByUsername("admin");
		
		if(username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
			session.setAttribute("username", "admin");
			session.setAttribute("admin", "admin");
			model.addAttribute("loggedIn",true);
			return "redirect:/admin/dashboard";
		}
		model.addAttribute("loggedIn",false);
		Users user = userService.checkUser(username, password);
		
		if(user != null) {
			session.setAttribute("username", username);
			model.addAttribute("loggedIn",true);
			return "redirect:/";
		}else {
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/logout")
	public String destroySession(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/login";
	}
	
	/*
	 * @PostMapping("/checkLogin") public String checkUser(@RequestParam("username")
	 * String username,
	 * 
	 * @RequestParam("password") String password, HttpSession session,Model model) {
	 * 
	 * Users admin = userService.findByUsername("admin");
	 * 
	 * if(username.equals(admin.getUsername()) &&
	 * password.equals(admin.getPassword())) { session.setAttribute("username",
	 * "admin"); session.setAttribute("admin", "admin");
	 * model.addAttribute("loggedIn",true); return "Admin Login Successful"; }
	 * model.addAttribute("loggedIn",false); Users user =
	 * userService.checkUser(username, password);
	 * 
	 * if(user != null) { session.setAttribute("username", username);
	 * model.addAttribute("loggedIn",true); return "User Login Successfull"; }else {
	 * return "Invalid Credentials"; }
	 * 
	 * }
	 */
}

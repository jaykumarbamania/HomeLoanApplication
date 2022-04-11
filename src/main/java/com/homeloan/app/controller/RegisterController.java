package com.homeloan.app.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.homeloan.app.model.SavingAccount;
import com.homeloan.app.model.Users;
import com.homeloan.app.repo.SavingAccountRepository;
import com.homeloan.app.service.SavingAccountService;
import com.homeloan.app.service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SavingAccountService savingService;
	
	@GetMapping("/register")
	public ModelAndView getRegisterPage() {
		Users user = new Users();
		
		String viewName = "register";
		String modelName = "user";
		
		return new ModelAndView(viewName, modelName, user);
	}
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
	        produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody ModelAndView registerUser( Users user,@RequestParam("phone") String phone,@RequestParam("cpassword") String cpass) {
		
		System.out.println(savingService.getLastAccountNo());
		if(cpass.equals(user.getPassword())) {
			Users userforEmail = userService.checkEmail(user.getEmail());
			Users userforPhone = userService.checkPhoneNum(Long.parseLong(phone));
			
			if(userforEmail == null && userforPhone ==null ) {
				SavingAccount currUser = new SavingAccount();
				currUser.setUser(user);
				currUser.setBalance((double) 0);
				currUser.setAccountno(savingService.getLastAccountNo()+1);
				currUser.setCreatedat(new Date());
				currUser.setUpdatedat(new Date());
				userService.saveUser(user);
				savingService.saveAccount(currUser);
				
				
			}else {
				return new ModelAndView("redirect:/register", "user", user);
			}
		}else {
			return new ModelAndView("redirect:/login", "user", user);
		}
		
		return new ModelAndView("redirect:/login", "user", user);
	}
}

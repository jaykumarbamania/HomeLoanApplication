package com.homeloan.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.homeloan.app.model.Users;
import com.homeloan.app.service.UserService;




@Controller
//@RequestMapping(value = "/")
public class LoginController {
//	@Autowired
//	private ILoginService loginService;
//	@Autowired
//	private IAdminService adminService;
//	@Autowired
//	private ICustomerService customerService;
	
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
			return "login";
		}
		
	}
	
	@GetMapping("/logout")
	public String destroySession(HttpServletRequest request) {
		request.getSession().invalidate();
		return "redirect:/login";
	}
	

//
//	@RequestMapping(value = "checkLogin", method = RequestMethod.POST)
//	public String checkLogin(@ModelAttribute(value = "loginObj") @Validated Login loginObj, BindingResult bindingResult,
//			Model model, HttpSession session) throws LoginException, AdminException {
//		String viewName = "";
//		if (bindingResult.hasErrors()) {// validations
//			model.addAttribute("msg", "Login Failed!");
//			model.addAttribute("loginObj", new Login());
//			viewName = "redirect:showLogin";
//			// prefix redirect is used to redirect from one controller method to the another
//			// method
//			}
//
//		Login loginResult = this.loginService.authenticateCustomer(loginObj);
//
//		if (loginResult == null) {
//
//			model.addAttribute("loginObj", new Login());
//			model.addAttribute("msg", "Login Falied Invalid Credentials!");
//			viewName = "loginPage";
//
//		} else {
//
//			model.addAttribute("loginObj", loginObj);
//			model.addAttribute("msg", "Login Successful!");
//			if (loginObj.getRole().equals("admin")) {
//				model.addAttribute("customer", new Customer());
//				List<Customer> idList = adminService.getCustomerList();
//				model.addAttribute("idList", idList);
//				System.out.println("Usernname: "+loginObj.getUsername());
//				//session.setAttribute("username", loginObj.getUsername());
//				viewName = "adminSucess";
//			}
//			if (loginObj.getRole().equals("user")) {
//				session.setAttribute("username",loginObj.getUsername());
//				viewName = "redirect:customerDetails";
//			}
//		}
//		return viewName;
//	}
//	
//	@RequestMapping(value = "/logout")
//	public String logout(Model model, HttpSession session) throws LoanException{
//		session.removeAttribute("username");
//		session.invalidate();
//		return "/homePage";
//	}
//	
//	@RequestMapping(value="/customerDetails")
//	public String showStatus( Model model, HttpSession session) throws LoanException {
//				String viewName="";
//				String username=(String) session.getAttribute("username");
//				/*System.out.println("HELOOOOOOO"+username);*/
//				model.addAttribute("customer", new Customer());
//				List<Customer> customerList=customerService.getCustomerList(username,session);
//				customerList.forEach((data)->System.out.println(data));
//				model.addAttribute("customerList",customerList);
//				System.err.println("HELOOOOOOO"+customerList);
//				viewName="successPage";
//				return viewName;
//	}
}

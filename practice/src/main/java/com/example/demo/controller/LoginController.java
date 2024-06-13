package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.LoginForm;
import com.example.demo.form.UserForm;
import com.example.demo.service.UserServiceImpl;

@Controller
public class LoginController {
	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping("/admin/signup")
	public String showUserForm(Model model) {
		model.addAttribute("userForm", new UserForm());
		return "signup"; // テンプレートのパス
	}

	@PostMapping("/admin/signup")
	public String registerUser(@Validated @ModelAttribute("userForm") UserForm userForm, BindingResult errorResult) {

		if (errorResult.hasErrors()) {
			return "signup";
		} else {
			userServiceImpl.saveUser(userForm);

			return "redirect:/admin/signin";
		}
	}

	@GetMapping("/admin/signin")
	public String showLoginForm(Model model) {
		model.addAttribute("loginForm", new LoginForm()); 

		return "signin"; 
	}

}
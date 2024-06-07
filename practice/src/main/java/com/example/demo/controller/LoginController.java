package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.LoginForm;
import com.example.demo.form.UserForm;
import com.example.demo.service.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    
    @GetMapping("/admin/signup")
    public String showUserForm(Model model) {
        System.out.println("AA");
        model.addAttribute("userForm", new UserForm());
        return "signup"; // テンプレートのパス
    }

    @PostMapping("/admin/signup")
    public String registerUser(@Validated @ModelAttribute("userForm") UserForm userForm, BindingResult errorResult) {
        System.out.println("BB");
        if (errorResult.hasErrors()) {
            return "signup";
        }else {
        userServiceImpl.saveUser(userForm);
        System.out.println("CC");
        return "redirect:/admin/signin";
    }
  }
    @GetMapping("/admin/signin")
    public String showLoginForm(Model model) {
        System.out.println("DD");
        model.addAttribute("loginForm", new LoginForm()); // loginForm をモデルに追加する 
    
        return "signin"; // signin.html のパスを返す
    }

    @PostMapping("/admin/signin")
    public String authenticateUser(@Validated @ModelAttribute("loginForm") LoginForm loginForm, RedirectAttributes redirectAttributes, HttpSession session) {
        System.out.println("FF");
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        if (userServiceImpl.authenticateUser(email, password)) {
            session.setAttribute("loggedIn", true);
            redirectAttributes.addFlashAttribute("successMessage", "Logged in successful!");
            return "redirect:/admin/contacts";
        } else {
            session.setAttribute("errorMessage", "Incorrect email or password");
            return "redirect:/admin/signin";
        }
    }
}

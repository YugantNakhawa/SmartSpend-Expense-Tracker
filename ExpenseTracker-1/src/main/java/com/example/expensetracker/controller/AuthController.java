package com.example.expensetracker.controller;

import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        userRepository.save(user);

        // Redirect back to login page with a message
        return "redirect:/auth/login?registered";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            return "Login successful!";
        } else {
            return "Invalid credentials!";
        }
    }
    
    
    @GetMapping("/auth/login")
    public String login() {
        return "auth/login"; // Thymeleaf will look for templates/auth/login.html
    }

    @GetMapping("/auth/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User()); // bind empty user for form
        return "auth/signup"; // templates/auth/signup.html
    }
}

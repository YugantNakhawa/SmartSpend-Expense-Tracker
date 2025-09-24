package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.EmailService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final EmailService emailService;

    public AdminController(UserRepository userRepository, ExpenseRepository expenseRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.emailService = emailService;
    }

    // Admin dashboard
    @GetMapping("")
    public String adminDashboard(Model model) {
        List<User> users = userRepository.findAll();
        List<Expense> expenses = expenseRepository.getAllExpenses();

        model.addAttribute("users", users);
        model.addAttribute("expenses", expenses);
        return "dashboard/admin"; // the Thymeleaf template
    }
    
    @PostMapping("/add-user")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("email") String email,
                          @RequestParam("role") String role) {

        User user = new User();
        user.setId(generateUserId()); // Assign ID manually
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setPassword("user@123"); // default password

        userRepository.save(user); // Save the user

        return "redirect:/admin";
    }

    private String generateUserId() {
        long count = userRepository.count(); // or another way to find max id
        return String.format("USER%03d", count + 1);
    }


    // Delete user
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

    // Delete expense
    @GetMapping("/delete-expense/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        expenseRepository.deleteExpense(id);
        return "redirect:/admin";
    }
    
    @GetMapping("/test-email")
    @ResponseBody
    public String testEmail() {
        emailService.sendEmail("booksmerit@gmail.com", "Test Email", "This is a test!");
        return "Email sent!";
    }

}

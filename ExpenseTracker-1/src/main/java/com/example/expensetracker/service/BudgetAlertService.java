package com.example.expensetracker.service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BudgetAlertService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private final double BUDGET_LIMIT = 10000.0; // ₹10,000 per month

    public BudgetAlertService(ExpenseRepository expenseRepository,
            UserRepository userRepository,
            EmailService emailService) {
				this.expenseRepository = expenseRepository;
				this.userRepository = userRepository;  // <--- initialize it
				this.emailService = emailService;
				}

    // Run every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkBudgetLimits() {
        List<User> users = userRepository.findAll();
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        for (User user : users) {
            List<Expense> expenses = expenseRepository.getExpensesByUser(user);
            double monthlyTotal = expenses.stream()
                    .filter(e -> !e.getDate().isBefore(startOfMonth) && !e.getDate().isAfter(endOfMonth))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            if (monthlyTotal > BUDGET_LIMIT) {
                emailService.sendEmail(
                        user.getEmail(),
                        "Budget Limit Alert",
                        "You have crossed your monthly budget limit of ₹10,000. Current total: ₹" + monthlyTotal
                );
            }
        }
    }
}

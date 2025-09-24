package com.example.expensetracker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    private final ExpenseRepository expenseRepository;
    private final EmailService emailService; // your JavaMailSender wrapper

    public ReminderService(ExpenseRepository expenseRepository, EmailService emailService) {
        this.expenseRepository = expenseRepository;
        this.emailService = emailService;
    }

    // Every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void upcomingBills() {
        LocalDate today = LocalDate.now();
        LocalDate reminderDate = today.plusDays(3); // 3 days before

        List<Expense> upcoming = expenseRepository.getAllExpenses().stream()
                .filter(e -> e.getDate().isEqual(reminderDate))
                .toList();

        for (Expense expense : upcoming) {
            emailService.sendEmail(
                    expense.getUser().getEmail(),
                    "Upcoming Bill Reminder",
                    "Reminder: You have an upcoming expense '" + expense.getTitle() +
                    "' of ₹" + expense.getAmount() +
                    " due on " + expense.getDate()
            );
        }
    }
}


package com.example.expensetracker.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.expensetracker.model.Expense;

@Service
public interface ExpenseService {
	
	Expense saveExpense(Expense expense);
    List<Expense> getAllExpenses();
    Expense getExpenseById(Long id);
    Expense updateExpense(Long id, Expense expense);
    void deleteExpense(Long id);
    List<Expense> getExpensesByCategory(String category);
    List<Expense> getExpensesBetweenDates(LocalDate start, LocalDate end);

}

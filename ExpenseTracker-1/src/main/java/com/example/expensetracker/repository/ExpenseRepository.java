package com.example.expensetracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;

@Repository
public interface ExpenseRepository {
	
	Expense saveExpense(Expense expense);
    List<Expense> getAllExpenses();
    Expense getExpenseById(Long id);
    void deleteExpense(Long id);
    List<Expense> getByCategory(String category);
    List<Expense> getBetweenDates(LocalDate start, LocalDate end);
    List<Expense> getExpensesByUser(User user);

}

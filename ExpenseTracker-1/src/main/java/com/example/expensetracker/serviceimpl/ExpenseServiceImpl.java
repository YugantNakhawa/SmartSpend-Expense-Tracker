package com.example.expensetracker.serviceimpl;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.*;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private ExpenseRepository repository;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	@Transactional
	public Expense saveExpense(Expense expense) {
	    if (expense.getDate() == null) {
	        expense.setDate(LocalDate.now());
	    }

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    User user = userRepository.findByUsername(username);
	    expense.setUser(user);

	    // Save the expense
	    Expense savedExpense = repository.saveExpense(expense);

	    // --- Budget check ---
	    double monthlyTotal = repository.getExpensesByUser(user)
	            .stream()
	            .filter(e -> e.getDate().getMonthValue() == LocalDate.now().getMonthValue()
	                      && e.getDate().getYear() == LocalDate.now().getYear())
	            .mapToDouble(Expense::getAmount)
	            .sum();

	    double budgetLimit = 10000; // Monthly budget limit
	    System.out.println("Monthly total: " + monthlyTotal);

	    if (monthlyTotal > budgetLimit) {
	        System.out.println("Budget exceeded! Sending email to " + user.getEmail());
	        emailService.sendEmail(
	            user.getEmail(),
	            "Budget Alert",
	            "⚠️ You have exceeded your monthly budget of ₹" + budgetLimit
	        );
	    }

	    return savedExpense;
	}



    @Override
    public List<Expense> getAllExpenses() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Get logged-in user
        User user = userRepository.findByUsername(username);

        // If user is ADMIN → show all expenses
        if (user.getRole().equals("ADMIN")) {
            return repository.getAllExpenses();
        }

        // If user is USER → show only their expenses
        return repository.getExpensesByUser(user);
    }

    @Override
    public Expense getExpenseById(Long id) {
        return repository.getExpenseById(id);
    }

    @Override
    @Transactional
    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense expense = repository.getExpenseById(id);
        if (expense != null) {
            expense.setTitle(updatedExpense.getTitle());
            expense.setAmount(updatedExpense.getAmount());
            expense.setCategory(updatedExpense.getCategory());
            expense.setDate(updatedExpense.getDate());
            return repository.saveExpense(expense);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        repository.deleteExpense(id);
    }

    @Override
    public List<Expense> getExpensesByCategory(String category) {
        return repository.getByCategory(category);
    }

    @Override
    public List<Expense> getExpensesBetweenDates(LocalDate start, LocalDate end) {
        return repository.getBetweenDates(start, end);
    }


}

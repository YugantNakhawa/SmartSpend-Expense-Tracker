package com.example.expensetracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;

@RestController
public class ExpenseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    
    

    /**
     * Fetch all expenses as JSON
     */
    @GetMapping("/expenses")
    @ResponseBody
    public ResponseEntity<List<Expense>> getAllExpenses() {
        try {
            List<Expense> expenses = expenseService.getAllExpenses();
            LOGGER.info("Fetched {} expenses", expenses.size());
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error fetching expenses", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Save a new expense
     */
    @PostMapping("/expensesoutput")
    @ResponseBody
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        try {
            Expense savedExpense = expenseService.saveExpense(expense);
            LOGGER.info("Added new expense: {}", savedExpense.getTitle());
            return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error adding expense", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") Long id) {
        try {
            expenseService.deleteExpense(id);
            LOGGER.info("Deleted expense with id {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (Exception e) {
            LOGGER.error("Error deleting expense with id {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

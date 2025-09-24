package com.example.expensetracker.service;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repositoryimpl.ExpenseRepositoryImpl;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import java.time.LocalDate; import java.util.List;
import java.util.Map; 

@Service 
public class LLMService {
	private final OllamaChatModel ollamaChatModel;
	private final ExpenseRepositoryImpl expenseRepository;
	public LLMService(OllamaChatModel ollamaChatModel, ExpenseRepositoryImpl expenseRepository) {
		this.ollamaChatModel = ollamaChatModel;
		this.expenseRepository = expenseRepository;
		} 
	
	public String queryModel(String prompt, User user) {
		// Check if the prompt is a custom query 
		if (isCustomQuery(prompt)) {
			return handleCustomQuery(prompt, user);
			} 
		else { // Fallback to general LLM response 
			return ollamaChatModel.call(prompt); 
			} 
		} 
	
	private boolean isCustomQuery(String prompt) {
		String q = prompt.toLowerCase(); 
		return q.contains("total") || q.contains("spent") || q.contains("category") || q.contains("monthly");
		} 
	
	private String handleCustomQuery(String prompt, User user) {
		String q = prompt.toLowerCase(); 
		
		if (q.contains("total") || q.contains("spent")) { double total = expenseRepository.getTotalExpenseByUser(user);
		return "You've spent a total of $" + String.format("%.2f", total) + " across all transactions.";
		} 
		
		if (q.contains("category") || q.contains("categories")) { 
			List<Object[]> categoryTotals = expenseRepository.getCategoryTotalsByUser(user);
			if (categoryTotals.isEmpty()) { return "No expenses found.";
			} 
			
			Object[] top = categoryTotals.get(0);
			String topCategory = (String) top[0];
			Double total = (Double) top[1];
			return "Your top spending category is " + topCategory + " with $" + String.format("%.2f", total);
			} 
		
		if (q.contains("monthly")) {
			LocalDate now = LocalDate.now();
			LocalDate monthStart = LocalDate.of(now.getYear(), now.getMonth(), 1);
			double monthTotal = expenseRepository.getMonthlyExpenseByUser(user, monthStart); return "This month you've spent $" + String.format("%.2f", monthTotal) + ".";
			} 
		return "I'm not sure about that. Please ask something else related to your expenses."; 
		
	} 
	

		
		
	}

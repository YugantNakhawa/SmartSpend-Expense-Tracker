package com.example.expensetracker.repositoryimpl; 
import java.time.LocalDate; 
import java.util.List; 
import org.springframework.stereotype.Repository; 
import com.example.expensetracker.model.Expense; 
import com.example.expensetracker.model.User; 
import com.example.expensetracker.repository.ExpenseRepository; 
import jakarta.persistence.EntityManager; 
import jakarta.persistence.PersistenceContext; 
import jakarta.persistence.TypedQuery; 
import jakarta.transaction.Transactional; 

@Repository 
public class ExpenseRepositoryImpl implements ExpenseRepository {
	@PersistenceContext 
	private EntityManager entityManager; 
	@Override 
	public Expense saveExpense(Expense expense) {
		if (expense.getId() == null) {
			entityManager.persist(expense); 
			return expense; 
			} 
		else {
			return entityManager.merge(expense); 
			} 
		} 
	@Override 
	public List<Expense> getAllExpenses() {
		return entityManager.createQuery("SELECT e FROM Expense e", Expense.class).getResultList(); 
		} 
	
	
	@Override 
	public Expense getExpenseById(Long id) {
		return entityManager.find(Expense.class, id); 
		} 
	
	@Override 
	@Transactional 
	public void deleteExpense(Long id) { 
		Expense expense = getExpenseById(id); 
		if (expense != null) {
			entityManager.remove(expense); 
			} 
		} 
	
	@Override public List<Expense> getByCategory(String category) {
		return entityManager.createQuery("SELECT e FROM Expense e WHERE e.category = :cat", Expense.class) .setParameter("cat", category) .getResultList(); } @Override public List<Expense> getBetweenDates(LocalDate start, LocalDate end) { return entityManager.createQuery("SELECT e FROM Expense e WHERE e.date BETWEEN :start AND :end", Expense.class) .setParameter("start", start) .setParameter("end", end) .getResultList();
		
		} 
		
		
		public List<Expense> getExpensesByUser(User user) {
			return entityManager .createQuery("SELECT e FROM Expense e WHERE e.user = :user", Expense.class) .setParameter("user", user) .getResultList(); 
			} 
		
		public Double getTotalExpenseByUser(User user) {
			TypedQuery<Double> query = entityManager.createQuery( "SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user", Double.class); query.setParameter("user", user); Double total = query.getSingleResult();
			return total != null ? total : 0.0; 
			} 
		
		public List<Object[]> getCategoryTotalsByUser(User user) { 
			// Returns List of Object[] where index 0 = category name, index 1 = total amount 
					return entityManager.createQuery( "SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user = :user GROUP BY e.category ORDER BY SUM(e.amount) DESC", Object[].class) .setParameter("user", user) .getResultList(); 
					} 
		public Double getMonthlyExpenseByUser(User user, LocalDate monthStart) {
			TypedQuery<Double> query = entityManager.createQuery( "SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.date >= :monthStart", Double.class); query.setParameter("user", user); query.setParameter("monthStart", monthStart); 
			Double total = query.getSingleResult(); return total != null ? total : 0.0; 
			} 
		}
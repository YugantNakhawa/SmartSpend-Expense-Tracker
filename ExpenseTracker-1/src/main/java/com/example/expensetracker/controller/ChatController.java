package com.example.expensetracker.controller; 
import com.example.expensetracker.model.User; 
import com.example.expensetracker.repository.UserRepository; 
import com.example.expensetracker.service.LLMService; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.context.SecurityContextHolder; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.web.bind.annotation.*; 
import reactor.core.publisher.Mono; 
import java.util.Map;

@RestController 
@RequestMapping("/llm") 
public class ChatController { 
	private final LLMService llmService; 
	private final UserRepository userRepository; 
	public ChatController(LLMService llmService, UserRepository userRepository) { 
		this.llmService = llmService; 
		this.userRepository = userRepository; 
		} 
	
	@PostMapping("/ask") 
	public Mono<String> ask(@RequestBody Map<String, String> payload) 
	{ 
		String question = payload.get("question"); 
		if (question == null || question.isEmpty()) { 
			return Mono.just("Please provide a valid question."); 
			} 
		User user = getCurrentUser(); 
		if (user == null) {
			return Mono.just("User not authenticated."); 
			} 
		return Mono.fromCallable(() -> llmService.queryModel(question, user)); 
		} 
	
	private User getCurrentUser() { 
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		if (authentication == null || !authentication.isAuthenticated()) { System.out.println("DEBUG: No authenticated user found."); 
		return null;
		} 
		Object principal = authentication.getPrincipal(); 
		if (principal instanceof UserDetails) { 
			String username = ((UserDetails) principal).getUsername(); 
			System.out.println("DEBUG: Authenticated user: " + username); 
			return userRepository.findByUsername(username); 
			} 
		System.out.println("DEBUG: Principal is not UserDetails."); 
		return null; 
		} 
	}
package com.bank.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dao.UserDAO;
import com.bank.model.User;
import com.bank.request.TransactionRequest;
import com.bank.response.TransactionResponse;
import com.bank.service.AccountService;
import com.bank.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	UserService userService;

	@PostMapping("/deposit")
	private ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
		TransactionResponse response = new TransactionResponse();
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal(); 
		
		accountService.deposit(request, user);

		response.setMessage("Amount successfully deposited");
		response.setSuccess(true);
		UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
		response.setUser(userDAO);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<TransactionResponse> withdraw(
			@Valid @RequestBody TransactionRequest request) {
		TransactionResponse response = new TransactionResponse();
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		// Account Balance Check
		if (user != null && user.getAccount() != null &&
				user.getAccount().getAccountBalance().intValue() >= request.getAmount() ) {
			
			accountService.withdraw(request, user);
			response.setMessage("Withdraw successfully performed");
			response.setSuccess(true);
			UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
			response.setUser(userDAO);
		}else {
			response.setMessage("Sorry! you dont have sufficient amount to withdraw");
			response.setSuccess(false);
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	

}

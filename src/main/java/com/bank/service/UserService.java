package com.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dao.UserDAO;
import com.bank.model.User;


public interface UserService {

	UserDAO getUserDAO(User user);
	
}
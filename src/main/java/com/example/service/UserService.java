package com.example.service;

import java.util.List;

import com.example.entity.User;

public interface UserService {
	
	public List<User> getAllUser();
	public List<User> getUserByRole(String role);
	
	// For login
	public User authenticate(String name, String pass);
	
	public User addUser(User userDTO);
	public User updateUser(int id, User userDTO);
	public boolean deleteUser(int id);
}

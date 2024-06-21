package com.example.service;

import java.util.List;

import com.example.entity.User;

public interface UserService {
	
	public List<User> getAllUser();
	public List<User> getUserByRole(String role);
	public User findById(Integer id);
	// For login
	public User authenticate(String name, String pass);
	
	public User addUser(User userDTO);
	public User updateUser(User userDTO);
	public boolean deleteUser(Integer id);
}

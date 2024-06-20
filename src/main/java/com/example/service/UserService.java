package com.example.service;

import java.util.List;

import com.example.entity.User;

public interface UserService {

	public List<User> getAllUser();
	public List<User> getUserByRole(String role);

	// For login
	public User authenticate(String name, String pass);

	public User addUser(User user);
	public User updateUser(User user);
	public boolean deleteUser(int id);

	public User findUserById(int id);
}

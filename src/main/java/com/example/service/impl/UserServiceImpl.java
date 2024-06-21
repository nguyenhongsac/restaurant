package com.example.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService{
	
	UserRepository ur;
	
	@Override
	public List<User> getAllUser() {
		return ur.findAll();
	}

	@Override
	public List<User> getUserByRole(String role) {
		return ur.findAllByRole(role);
	}

	@Override
	public User authenticate(String name, String pass) {
		
		return ur.findByName(name).orElse(null);
	}

	@Override
	public User addUser(User user) {
		if (ur.existsByName(user.getName())) {
			return null;
		}
		
		return ur.save(user);
	}

	@Override
	public User updateUser(User u) {
		return ur.save(u);
	}


	@Override
	public boolean deleteUser(Integer id) {
		if (ur.existsById(id)) {
			ur.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		return ur.findById(id).get();
	}
	
}

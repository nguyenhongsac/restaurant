package com.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import lombok.AccessLevel;
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
		User u = ur.findByName(name).get();

		if (u!=null && pass.equals(u.getPassword())) {
			return u;
		}

		return null;
	}

	@Override
	public User addUser(User user) {
		return ur.save(user);
	}

	@Override
	public User updateUser(User u) {
		User um = ur.getReferenceById(u.getId());

		if (u.getName()!=null) {
			um.setName(u.getName());
		}
		if (u.getPassword()!=null) {
			um.setPassword(u.getPassword());
		}
		if (u.getFullname()!=null) {
			um.setFullname(u.getFullname());
		}
		if (u.getAvatar()!=null) {
			um.setAvatar(u.getAvatar());
		}
		if (u.getDob()!=null) {
			um.setDob(u.getDob());
		}
		if (u.getGender()!=null) {
			um.setGender(u.getGender());
		}
		if (u.getPhone()!=null) {
			um.setPhone(u.getPhone());
		}
		if (u.getAddress()!=null) {
			um.setAddress(u.getAddress());
		}
		if (u.getRole()!=null) {
			um.setRole(u.getRole());
		}
		if (u.getCreatedTime()!=null) {
			um.setCreatedTime(u.getCreatedTime());
		}
		if (u.getModifiedTime()!=null) {
			um.setModifiedTime(u.getModifiedTime());
		}
		if (u.getLastLogined()!=null) {
			um.setLastLogined(u.getLastLogined());
		}
		if (u.getPermission()!=null) {
			um.setPermission(u.getPermission());
		}
		if (u.getApplyyear()!=null) {
			um.setApplyyear(u.getApplyyear());
		}
		if (u.getDeleted()!=null) {
			um.setDeleted(u.getDeleted());
		}

		return ur.save(um);
	}


	@Override
	public boolean deleteUser(int id) {
		if (ur.existsById(id)) {
			ur.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User findUserById(int id) {
		// TODO Auto-generated method stub
		return ur.getReferenceById(id);
	}
}

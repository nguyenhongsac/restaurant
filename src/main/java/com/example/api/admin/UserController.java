package com.example.api.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.User;
import com.example.service.UserService;

import jakarta.annotation.PostConstruct;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/user")
	public String user(Model model) {
		List<User> user = this.userService.getAllUser();
		model.addAttribute("user",user);
		return "user-data";
	}
	@PostMapping("/add-user")
	public String add(@RequestParam String name,@RequestParam String pass,@RequestParam String fullname,@RequestParam String avatar,@RequestParam String dob,@RequestParam String gender,@RequestParam String role,@RequestParam String address,@RequestParam String phone) {
		User user = new User();
		user.setName(name);
		user.setPassword(pass);
		user.setFullname(fullname);
		user.setAvatar(avatar);
		user.setDob(dob);
		user.setPhone(phone);
		user.setAddress(address);
		user.setRole(role);
		user.setGender(gender);
		user.setCreatedTime(LocalDateTime.now());
		userService.addUser(user);
		return "redirect:/user";
	}
	@PostMapping("/update-user/{id}")
	public String postMethodName(@PathVariable Integer id, @RequestParam String name,@RequestParam String pass,@RequestParam String fullname,@RequestParam String avatar,@RequestParam String dob,@RequestParam String gender,@RequestParam String role,@RequestParam String address,@RequestParam String phone) {
		User user = this.userService.findUserById(id);
		user.setName(name);
		user.setPassword(pass);
		user.setFullname(fullname);
		user.setAvatar(avatar);
		user.setDob(dob);
		user.setPhone(phone);
		user.setAddress(address);
		user.setRole(role);
		user.setGender(gender);
		user.setModifiedTime(LocalDateTime.now());
		userService.updateUser(user);
		return "redirect:/user";
	}
	
}

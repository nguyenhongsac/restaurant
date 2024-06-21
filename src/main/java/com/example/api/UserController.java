package com.example.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal=true)
@RequestMapping("/api/user")
public class UserController {

	UserService userService;

	@GetMapping("/get")
	public User login(@RequestParam String username, @RequestParam String password) {
		User u = userService.authenticate(username, password);

		if (u.getPassword().equals(password)) {
			return u;
		}

		return null;
	}

	@PostMapping("/add") // missing password encoder
	public ResponseEntity<String> add(@RequestBody User userDTO) {
		if (userService.addUser(userDTO)!=null) {
			return ResponseEntity.ok().body("Add user successful!");
		}

		return ResponseEntity.ok().body("Add user failed!");
	}

	@PostMapping("/update")
	public ResponseEntity<String> update(@RequestParam int id, @RequestBody User userDTO) {
		if (userService.updateUser(userDTO)!=null) {
			return ResponseEntity.ok().body("Update user "+id+" successful!");
		}
		return ResponseEntity.ok().body("Update user "+id+" failed!");
	}

	@DeleteMapping("/del")
	public ResponseEntity<String> del(@RequestParam int id) {
		if (userService.deleteUser(id)) {
			return ResponseEntity.ok().body("Delete user "+id+" successful!");
		}
		return ResponseEntity.ok().body("Delete user "+id+" failed!");
	}
}

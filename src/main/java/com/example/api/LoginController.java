package com.example.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.User;
import com.example.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {

	UserService us;

	@GetMapping({"","/","/login"})
	public String start() {
		return "login";
	}

	@PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
		try {
	        User loggedUser = us.authenticate(username, password);
	        if (loggedUser != null) {
	        	if ("admin".equalsIgnoreCase(loggedUser.getRole())) {
	        		session.setAttribute("loggedUser", loggedUser);
		            return "redirect:/index";
	        	}
	        	session.setAttribute("loggedUser", loggedUser);
	            return "redirect:/home";

	        } else {
	            model.addAttribute("error", "Invalid username or password");
	            return "login";
	        }
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "An error occurred during authentication. Please try again.");
            return "login";
		}
    }

	@GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}

package com.example.api.table;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReserveTableController {

	@GetMapping("/guest")
	public String reserveGuest() {
		return "book-table";
	}
}

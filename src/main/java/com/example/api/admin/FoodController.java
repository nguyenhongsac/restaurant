package com.example.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.FoodEntity;
import com.example.service.FoodService;

@Controller
public class FoodController {
	
	@Autowired
	private FoodService foodService;
	@RequestMapping("/food")
	public String food(Model model) {
		List<FoodEntity> list = this.foodService.getAll(); 
		model.addAttribute("list", list);
		return "food-data";
	}
}

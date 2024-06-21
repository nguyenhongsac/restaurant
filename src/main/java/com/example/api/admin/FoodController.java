package com.example.api.admin;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.CategoryEntity;
import com.example.entity.FoodEntity;
import com.example.service.CategoryService;
import com.example.service.FoodService;

@Controller
public class FoodController {
	
	@Autowired
	private FoodService foodService;
	@Autowired
	private CategoryService categoryService;
	@GetMapping("/Food")
	public String food(Model model) {
		List<CategoryEntity> category = this.categoryService.getAll();
		model.addAttribute("category", category);
		List<FoodEntity> food = this.foodService.getAll(); 
		model.addAttribute("food", food);
		return "food-data";
	}
	@PostMapping("/add-food")
	public String addFood(@RequestParam String name, @RequestParam String img, @RequestParam float price, @RequestParam String note,@RequestParam String allergen,@RequestParam String ingredients,@RequestParam String catname, @RequestParam boolean avaiable) {
		FoodEntity food = new FoodEntity();
		CategoryEntity category = categoryService.findByName(catname);
		food.setCategory(category);
		food.setFoodName(name);
		food.setFoodImg(img);
		food.setFoodPrice(price);
		food.setFoodAvailable(avaiable);
		food.setFoodAllergenInfo(allergen);
		food.setFoodIngredients(ingredients);
		food.setFoodNote(note);
		food.setFoodCreatedTime(LocalDateTime.now());
		food.setFoodModifiedTime(LocalDateTime.now());
		foodService.save(food);
		return "redirect:/Food";
	}
	@PostMapping("/update-food/{foodId}")
	public String updateCategory( @PathVariable Integer foodId, @RequestParam String name, @RequestParam String img, @RequestParam float price, @RequestParam String note,@RequestParam String allergen,@RequestParam String ingredients,@RequestParam String catname, @RequestParam boolean avaiable ) {
		// Fetch the food by ID
		FoodEntity food = foodService.findById(foodId);
		CategoryEntity category = categoryService.findByName(catname);
		// Update the category with the form values
		food.setCategory(category);
		food.setFoodName(name);
		food.setFoodImg(img);
		food.setFoodPrice(price);
		food.setFoodAvailable(avaiable);
		food.setFoodAllergenInfo(allergen);
		food.setFoodIngredients(ingredients);
		food.setFoodNote(note);
		food.setFoodCreatedTime(LocalDateTime.now());
		food.setFoodModifiedTime(LocalDateTime.now());
		foodService.save(food);
		return "redirect:/Food";
	}

	@PostMapping("/delete-food/{foodId}")
	public String deleteCategory(@PathVariable Integer foodId) {
		foodService.delete(foodId);
		return "redirect:/Food";
	}
}

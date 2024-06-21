package com.example.api.admin;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.CategoryEntity;
import com.example.service.CategoryService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CategoryController {

	private CategoryService categoryService;

	@GetMapping("/cate")
	public String category(Model model) {
		List<CategoryEntity> category = this.categoryService.getAll();
		model.addAttribute("category", category);
		return "categories-data";
	}

	@PostMapping("/category")
	public String addCategory(@RequestParam String name, @RequestParam String description) {
		CategoryEntity category = new CategoryEntity();
		category.setCatName(name);
		category.setCatDescription(description);
		category.setCatCreatedTime(LocalDateTime.now());
		categoryService.create(category);
		return "redirect:/cate";
	}

	@PostMapping("/update-category/{catId}")
	public String updateCategory(@RequestParam String name, @RequestParam String description,
			@PathVariable("catId") Integer catId) {
		// Fetch the category by ID
		CategoryEntity category = categoryService.findById(catId);

		// Update the category with the form values
		category.setCatName(name);
		category.setCatDescription(description);
		category.setCatModifiedTime(LocalDateTime.now());
		// Save the updated category
		categoryService.save(category);

		// Redirect or return appropriate response
		return "redirect:/cate"; // assuming you have a view to show categories
	}

	@PostMapping("/delete-category/{catId}")
	public String deleteCategory(@PathVariable Integer catId) {
		categoryService.delete(catId);
		return "redirect:/cate";
	}
}

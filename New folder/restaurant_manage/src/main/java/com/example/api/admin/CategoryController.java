package com.example.api.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.CategoryEntity;
import com.example.service.CategoryService;

@Controller

public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@RequestMapping("/category")
	public String category(Model model) {
		List<CategoryEntity> category = this.categoryService.getAll();
		model.addAttribute("category", category);
		return "categories-data";
	}

	@PostMapping("/category")
	public String addCategory(@RequestParam String name, @RequestParam String description, Model model) {
		CategoryEntity category = new CategoryEntity();
		category.setCatName(name);
		category.setCatDescription(description);
		this.categoryService.create(category);
		return "redirect:/category";
	}
//	@ModelAttribute(value = "myCategory")
//	public CategoryEntity newEntity()
//	{
//	    return new CategoryEntity();
//	}
//	@GetMapping("/category/{catId}")
//	public String edit(Model model, @PathVariable("id") Integer id) {
//		CategoryEntity category = this.categoryService.findById(id);
//		model.addAttribute("category", category);
//		return "redirect:/category";
//	}
}

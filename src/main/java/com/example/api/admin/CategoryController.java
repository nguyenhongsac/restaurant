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
	public String addCategory(@RequestParam String name, @RequestParam String description, Model model) {
		CategoryEntity category = new CategoryEntity();
		category.setCatId(11);
		category.setCatName(name);
		category.setCatDescription(description);
		categoryService.create(category);
		System.out.println(category.getCatName());
		return "redirect:/cate";
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

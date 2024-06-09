package com.example.service;

import com.example.entity.CategoryEntity;
import com.example.entity.FoodEntity;
import com.example.repository.CategoryRepository;
import com.example.repository.FoodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
	@Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}


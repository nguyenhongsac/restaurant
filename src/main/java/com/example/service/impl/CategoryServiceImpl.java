package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.CategoryEntity;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Override
	public List<CategoryEntity> getAll() {
		// TODO Auto-generated method stub
		return this.categoryRepository.findAll();
	}

	@Override
	public CategoryEntity findById(Integer catId) {
		// TODO Auto-generated method stub

		return this.categoryRepository.findById(catId).get();
	}

	@Override
	public Boolean create(CategoryEntity category) {
		try {
			this.categoryRepository.save(category);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean edit(CategoryEntity category) {
		// TODO Auto-generated method stub
		try {
			this.categoryRepository.save(category);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean delete(Integer catId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(CategoryEntity category) {
		// TODO Auto-generated method stub
		 categoryRepository.save(category);
	}

}

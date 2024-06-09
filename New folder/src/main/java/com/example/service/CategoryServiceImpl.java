package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.CategoryEntity;
import com.example.repository.CategoryRepository;
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
		return null;
	}

	@Override
	public Boolean create(CategoryEntity category) {
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
	public Boolean edit(CategoryEntity category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Integer catId) {
		// TODO Auto-generated method stub
		return null;
	}

}
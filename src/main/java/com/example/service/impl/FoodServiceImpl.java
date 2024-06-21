package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.FoodEntity;
import com.example.repository.FoodRepository;
import com.example.service.FoodService;
@Service
public class FoodServiceImpl implements FoodService {
	@Autowired
	private FoodRepository foodRepository;
	@Override public List<FoodEntity> getAll() {
		// TODO Auto-generated method stub
		return this.foodRepository.findAll();
	}

	@Override
	public FoodEntity findById(Integer foodId) {
		// TODO Auto-generated method stub
		return this.foodRepository.findById(foodId).get();
	}

	@Override
	public Boolean create(FoodEntity food) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean edit(FoodEntity food) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Integer foodId) {
		// TODO Auto-generated method stub
		try {
			this.foodRepository.delete(findById(foodId));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void save(FoodEntity food) {
		// TODO Auto-generated method stub
		foodRepository.save(food);
	}

}

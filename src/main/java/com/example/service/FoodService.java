package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.FoodEntity;
import com.example.repository.FoodRepository;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<FoodEntity> getAllFoods() {
        return foodRepository.findAll();
    }

	public FoodEntity findFoodById(Integer foodId) {
		return foodRepository.findByFoodId(foodId);
	}
}


package com.example.service;

import java.util.List;

import com.example.entity.FoodEntity;

public interface FoodService {
	List<FoodEntity> getAll();
	FoodEntity findById(Integer foodId);
	Boolean create (FoodEntity food);
	Boolean edit (FoodEntity food);
	Boolean delete(Integer foodId);
	void save(FoodEntity food);
}

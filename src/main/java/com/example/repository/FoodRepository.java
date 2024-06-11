package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.FoodEntity;
import com.example.entity.OrderDetail;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Integer> {
	@Query("SELECT fd FROM FoodEntity fd WHERE fd.foodId = :foodId")
    FoodEntity findByFoodId(Integer foodId);
}


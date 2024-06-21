package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.FoodEntity;

public interface FoodRepository extends JpaRepository<FoodEntity, Integer> {

}

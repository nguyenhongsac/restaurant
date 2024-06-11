package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.CategoryEntity;
import com.example.entity.FoodEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}

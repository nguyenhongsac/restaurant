package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query("SELECT od FROM Order od WHERE od.order_id = :orderId")
    Order findByOrderId(Integer orderId);
}


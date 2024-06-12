package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Order;
import com.example.entity.OrderDetail;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query("SELECT od FROM Order od WHERE od.order_id = :orderId")
    Order findByOrderId(Integer orderId);
	@Query("SELECT od FROM Order od WHERE od.table_id = :tableId")
	List<Order> findByTableId(Integer tableId);
}


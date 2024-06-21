package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	@Query("SELECT od FROM OrderDetail od WHERE od.order.order_id = :orderId")
    List<OrderDetail> findByOrderId(Integer orderId);

	
	@Query("SELECT od FROM OrderDetail od WHERE od.order.table_id = :tableId")
    List<OrderDetail> findByTableId(Integer tableId);

}


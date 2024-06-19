package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Order;
import com.example.entity.OrderDetail;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query("SELECT od FROM Order od WHERE od.order_id = :orderId")
	Order findByOrderId(Integer orderId);

	@Query("SELECT od FROM Order od WHERE od.table_id = :tableId")
	Order findByTableId(Integer tableId);

	@Query(value = "SELECT o.* FROM `tblorder` o JOIN tblbill b ON o.bill_id = b.bill_id WHERE o.table_id = :tableId AND b.bill_status = 'null' ORDER BY o.order_created_time DESC LIMIT 1", nativeQuery = true)
	Order findLatestOrderByTableId(@Param("tableId") Integer tableId);
}

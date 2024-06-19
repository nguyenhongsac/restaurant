package com.example.service;

import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    public List<Order> getAll() {
		return orderRepository.findAll();
	}

    public void addOrder(Order order) {
		orderRepository.save(order);
	}
    
    public void deleteOrder(Integer orderId) {
	    orderRepository.deleteById(orderId);
    }
    
	public void updateOrder(Order order) {
		orderRepository.save(order);
	}
	
	public Order getOrderByTable(Integer tableId) {
		return orderRepository.findByTableId(tableId);
		
	}
    public Order getOrderByOrder(Integer orderId) {
        return orderRepository.findByOrderId(orderId);
    }
    
    public Order getLatestOrderByTableId(Integer tableId) {
        return orderRepository.findLatestOrderByTableId(tableId);
    }
}


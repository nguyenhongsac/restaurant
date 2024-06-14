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

    public Order getOrderByOrder(Integer orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}

